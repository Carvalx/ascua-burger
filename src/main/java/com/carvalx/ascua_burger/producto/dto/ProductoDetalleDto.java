package com.carvalx.ascua_burger.producto.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

// Versión completa para la pantalla de detalle de producto
// Incluye ingredientes para mostrar la personalización
@Data
public class ProductoDetalleDto {
    private UUID id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioBase;
    private String imagenUrl;
    private Boolean disponible;
    private CategoriaDto categoria;
    // Lista de ingredientes con si vienen por defecto o son extras
    private List<ProductoIngredienteDto> ingredientes;
}