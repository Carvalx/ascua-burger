package com.carvalx.ascua_burger.producto.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

// Entrada del admin para crear un producto
// Separado del ProductoDto porque lo que entra nunca es igual a lo que sale
// Por ejemplo: entra categoria_id (UUID), sale categoria (objeto con nombre y slug)
@Data
public class CrearProductoDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
    private BigDecimal precioBase;

    @NotNull(message = "La categoría es obligatoria")
    private UUID categoriaId;

    private String imagenUrl;
}