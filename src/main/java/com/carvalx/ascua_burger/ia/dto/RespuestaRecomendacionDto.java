package com.carvalx.ascua_burger.ia.dto;

import lombok.Data;

import java.util.List;

@Data
public class RespuestaRecomendacionDto {
    private List<RecomendacionDto> recomendaciones;
    private String mensaje;
}