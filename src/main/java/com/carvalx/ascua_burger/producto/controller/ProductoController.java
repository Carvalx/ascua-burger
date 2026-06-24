package com.carvalx.ascua_burger.producto.controller;

import com.carvalx.ascua_burger.producto.dto.CrearProductoDto;
import com.carvalx.ascua_burger.producto.dto.ProductoDetalleDto;
import com.carvalx.ascua_burger.producto.dto.ProductoDto;
import com.carvalx.ascua_burger.producto.service.ProductoService;
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
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // GET /api/productos?page=0&size=6&sort=nombre,asc
    // @PageableDefault define los valores por defecto si el cliente no los manda
    // Público — la carta es visible sin login
    @GetMapping
    public ResponseEntity<Page<ProductoDto>> listar(
            @PageableDefault(size = 6, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(productoService.listarDisponibles(pageable));
    }

    // GET /api/productos?categoriaId=uuid&page=0&size=6
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<Page<ProductoDto>> listarPorCategoria(
            @PathVariable UUID categoriaId,
            @PageableDefault(size = 6) Pageable pageable) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoriaId, pageable));
    }

    // GET /api/productos/buscar?nombre=roble
    @GetMapping("/buscar")
    public ResponseEntity<Page<ProductoDto>> buscar(
            @RequestParam String nombre,
            @PageableDefault(size = 6) Pageable pageable) {
        return ResponseEntity.ok(productoService.buscar(nombre, pageable));
    }

    // GET /api/productos/{id} — detalle con ingredientes
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDetalleDto> detalle(@PathVariable UUID id) {
        return ResponseEntity.ok(productoService.obtenerDetalle(id));
    }

    // POST /api/productos — solo ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDto> crear(@Valid @RequestBody CrearProductoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.crear(dto));
    }

    // PATCH /api/productos/{id}/disponibilidad — activar/desactivar
    @PatchMapping("/{id}/disponibilidad")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDto> toggleDisponibilidad(@PathVariable UUID id) {
        return ResponseEntity.ok(productoService.toggleDisponibilidad(id));
    }
}