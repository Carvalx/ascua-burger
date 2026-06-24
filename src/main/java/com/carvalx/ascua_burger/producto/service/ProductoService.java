package com.carvalx.ascua_burger.producto.service;

import com.carvalx.ascua_burger.producto.domain.Producto;
import com.carvalx.ascua_burger.producto.dto.CrearProductoDto;
import com.carvalx.ascua_burger.producto.dto.ProductoDetalleDto;
import com.carvalx.ascua_burger.producto.dto.ProductoDto;
import com.carvalx.ascua_burger.producto.mapper.ProductoMapper;
import com.carvalx.ascua_burger.producto.repository.CategoriaRepository;
import com.carvalx.ascua_burger.producto.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;

    // Page<ProductoDto> devuelve no solo los datos sino también
    // totalElements, totalPages, currentPage — perfecto para paginación en React
    // El cliente manda: GET /api/productos?page=0&size=6
    @Transactional(readOnly = true)
    public Page<ProductoDto> listarDisponibles(Pageable pageable) {
        return productoRepository
                .findByDisponibleTrue(pageable)
                .map(productoMapper::toDto);
    }

    // Filtrar por categoría — para los botones de la carta
    @Transactional(readOnly = true)
    public Page<ProductoDto> listarPorCategoria(UUID categoriaId, Pageable pageable) {
        return productoRepository
                .findByCategoriaIdAndDisponibleTrue(categoriaId, pageable)
                .map(productoMapper::toDto);
    }

    // Buscador — para la barra de búsqueda del frontend
    @Transactional(readOnly = true)
    public Page<ProductoDto> buscar(String nombre, Pageable pageable) {
        return productoRepository
                .findByNombreContainingIgnoreCaseAndDisponibleTrue(nombre, pageable)
                .map(productoMapper::toDto);
    }

    // Detalle con ingredientes — usa JOIN FETCH para evitar N+1
    @Transactional(readOnly = true)
    public ProductoDetalleDto obtenerDetalle(UUID id) {
        Producto producto = productoRepository.findByIdWithCategoria(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return productoMapper.toDetalleDto(producto);
    }

    // Crear producto — solo ADMIN
    // En el controller añadiremos @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductoDto crear(CrearProductoDto dto) {
        var categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precioBase(dto.getPrecioBase())
                .categoria(categoria)
                .imagenUrl(dto.getImagenUrl())
                .disponible(true)
                .build();

        return productoMapper.toDto(productoRepository.save(producto));
    }

    // Activar/desactivar producto sin borrarlo
    // Útil cuando se acaba un ingrediente principal temporalmente
    @Transactional
    public ProductoDto toggleDisponibilidad(UUID id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setDisponible(!producto.getDisponible());
        return productoMapper.toDto(productoRepository.save(producto));
    }
}