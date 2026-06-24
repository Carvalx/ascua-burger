package com.carvalx.ascua_burger.pedido.service;

import com.carvalx.ascua_burger.pedido.domain.EstadoPedido;
import com.carvalx.ascua_burger.pedido.domain.LineaPedido;
import com.carvalx.ascua_burger.pedido.domain.Pedido;
import com.carvalx.ascua_burger.pedido.dto.ActualizarEstadoDto;
import com.carvalx.ascua_burger.pedido.dto.CrearPedidoDto;
import com.carvalx.ascua_burger.pedido.dto.PedidoDto;
import com.carvalx.ascua_burger.pedido.mapper.PedidoMapper;
import com.carvalx.ascua_burger.pedido.repository.PedidoRepository;
import com.carvalx.ascua_burger.producto.domain.Producto;
import com.carvalx.ascua_burger.producto.repository.ProductoRepository;
import com.carvalx.ascua_burger.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final PedidoMapper pedidoMapper;

    // @Transactional sin readOnly porque escribe en BD
    // Si cualquier paso falla (producto no existe, BD caída) todo el pedido
    // se deshace — no quedan pedidos a medias en la BD
    // Esto es lo que preguntarán en CPS: "¿qué hace @Transactional?"
    @Transactional
    public PedidoDto crear(CrearPedidoDto dto) {
        // Obtenemos el usuario del contexto de seguridad — el que tiene el JWT
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .build();

        // Por cada línea del pedido, verificamos que el producto existe
        // y guardamos el precio actual (no el futuro)
        dto.getLineas().forEach(lineaDto -> {
            Producto producto = productoRepository.findById(lineaDto.getProductoId())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado: " + lineaDto.getProductoId()));

            if (!producto.getDisponible()) {
                throw new RuntimeException("Producto no disponible: " + producto.getNombre());
            }

            LineaPedido linea = LineaPedido.builder()
                    .producto(producto)
                    .cantidad(lineaDto.getCantidad())
                    .precioUnitario(producto.getPrecioBase())
                    .personalizaciones(lineaDto.getPersonalizaciones())
                    .build();

            // agregarLinea recalcula el total automáticamente
            pedido.agregarLinea(linea);
        });

        return pedidoMapper.toDto(pedidoRepository.save(pedido));
    }

    // Mis pedidos — solo los del usuario autenticado
    @Transactional(readOnly = true)
    public Page<PedidoDto> misPedidos(Pageable pageable) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return pedidoRepository
                .findByUsuarioIdOrderByCreatedAtDesc(usuario.getId(), pageable)
                .map(pedidoMapper::toDto);
    }

    // Detalle de un pedido con sus líneas
    @Transactional(readOnly = true)
    public PedidoDto obtener(UUID id) {
        return pedidoMapper.toDto(
                pedidoRepository.findByIdWithLineas(id)
                        .orElseThrow(() -> new RuntimeException("Pedido no encontrado")));
    }

    // Cambiar estado — solo ADMIN o COCINA
    // Validamos que la transición sea válida
    @Transactional
    public PedidoDto actualizarEstado(UUID id, ActualizarEstadoDto dto) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        validarTransicion(pedido.getEstado(), dto.getEstado());
        pedido.setEstado(dto.getEstado());

        return pedidoMapper.toDto(pedidoRepository.save(pedido));
    }

    // Cancelar — el propio cliente puede cancelar si está en RECIBIDO
    @Transactional
    public PedidoDto cancelar(UUID id) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No puedes cancelar un pedido que no es tuyo");
        }

        if (pedido.getEstado() != EstadoPedido.RECIBIDO) {
            throw new RuntimeException("Solo se puede cancelar un pedido en estado RECIBIDO");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        return pedidoMapper.toDto(pedidoRepository.save(pedido));
    }

    // Validar que la transición de estados sea coherente
    // No puedes ir de ENTREGADO a EN_PREPARACION, por ejemplo
    private void validarTransicion(EstadoPedido actual, EstadoPedido nuevo) {
        boolean valida = switch (actual) {
            case RECIBIDO -> nuevo == EstadoPedido.EN_PREPARACION
                    || nuevo == EstadoPedido.CANCELADO;
            case EN_PREPARACION -> nuevo == EstadoPedido.LISTO
                    || nuevo == EstadoPedido.CANCELADO;
            case LISTO -> nuevo == EstadoPedido.ENTREGADO;
            case ENTREGADO, CANCELADO -> false;
        };

        if (!valida) {
            throw new RuntimeException(
                    "Transición inválida: " + actual + " → " + nuevo);
        }
    }
}