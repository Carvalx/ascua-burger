package com.carvalx.ascua_burger.producto.dto;

import lombok.Data;

import java.util.UUID;

// Lo que devolvemos al cliente cuando pide categorías
// Solo los campos que necesita el frontend — nada de created_at ni la lista de productos
@Data
public class CategoriaDto {
    private UUID id;
    private String nombre;
    private String slug;
}