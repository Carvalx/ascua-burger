package com.carvalx.ascua_burger.producto.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class IngredienteDto {
    private UUID id;
    private String nombre;
    private BigDecimal precioExtra;
    private Boolean esAlergeno;
    // No exponemos el stock al cliente — es info interna de cocina
    // Si quisieras un panel de cocina harías un IngredienteAdminDto con el stock
}