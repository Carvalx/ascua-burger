package com.carvalx.ascua_burger.producto.controller;

import com.carvalx.ascua_burger.producto.dto.CategoriaDto;
import com.carvalx.ascua_burger.producto.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // Público — cualquiera puede ver las categorías de la carta
    @GetMapping
    public ResponseEntity<List<CategoriaDto>> listar() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    // Solo ADMIN puede crear categorías
    // @PreAuthorize evalúa el rol del token JWT antes de entrar al método
    // Si el rol no es ADMIN devuelve 403 automáticamente
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDto> crear(@RequestParam String nombre) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.crear(nombre));
    }
}