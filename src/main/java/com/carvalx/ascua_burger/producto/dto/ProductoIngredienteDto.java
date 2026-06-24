package com.carvalx.ascua_burger.producto.dto;

import lombok.Data;

// Lo que ve el cliente al abrir el detalle de una hamburguesa
// "Cheddar curado — incluido" / "Trufa rallada — +3,00€ extra"
@Data
public class ProductoIngredienteDto {
    private IngredienteDto ingrediente;
    private Boolean incluidoPorDefecto;
}