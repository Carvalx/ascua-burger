package com.carvalx.ascua_burger.producto.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

// Versión ligera para el listado de la carta
// No incluye ingredientes — se cargan solo en el detalle
// Esto reduce el tamaño de la respuesta en el listado paginado
@Data
public class ProductoDto {
    private UUID id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioBase;
    private String imagenUrl;
    private Boolean disponible;
    private CategoriaDto categoria;
}