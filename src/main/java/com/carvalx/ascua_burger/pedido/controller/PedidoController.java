package com.carvalx.ascua_burger.pedido.controller;

import com.carvalx.ascua_burger.pedido.dto.ActualizarEstadoDto;
import com.carvalx.ascua_burger.pedido.dto.CrearPedidoDto;
import com.carvalx.ascua_burger.pedido.dto.PedidoDto;
import com.carvalx.ascua_burger.pedido.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // Crear pedido — cualquier usuario autenticado
    @PostMapping
    public ResponseEntity<PedidoDto> crear(@Valid @RequestBody CrearPedidoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.crear(dto));
    }

    // Mis pedidos — solo los del usuario autenticado
    @GetMapping("/mis-pedidos")
    public ResponseEntity<Page<PedidoDto>> misPedidos(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(pedidoService.misPedidos(pageable));
    }

    // Detalle de un pedido
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDto> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(pedidoService.obtener(id));
    }

    // Cambiar estado — solo ADMIN o COCINA
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COCINA')")
    public ResponseEntity<PedidoDto> actualizarEstado(
            @PathVariable UUID id,
            @Valid @RequestBody ActualizarEstadoDto dto) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, dto));
    }

    // Cancelar — el propio cliente
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoDto> cancelar(@PathVariable UUID id) {
        return ResponseEntity.ok(pedidoService.cancelar(id));
    }
}