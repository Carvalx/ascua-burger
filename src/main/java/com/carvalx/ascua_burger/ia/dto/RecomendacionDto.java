package com.carvalx.ascua_burger.ia.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class RecomendacionDto {
    private UUID productoId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String razon;
}  