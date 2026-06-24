package com.carvalx.ascua_burger.ia.controller;

import com.carvalx.ascua_burger.ia.dto.PreferenciasDto;
import com.carvalx.ascua_burger.ia.dto.RespuestaRecomendacionDto;
import com.carvalx.ascua_burger.ia.service.RecomendadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recomendaciones")
@RequiredArgsConstructor
public class RecomendadorController {

    private final RecomendadorService recomendadorService;

    @PostMapping
    public ResponseEntity<RespuestaRecomendacionDto> recomendar(
            @Valid @RequestBody PreferenciasDto dto) {
        return ResponseEntity.ok(recomendadorService.recomendar(dto));
    }
}