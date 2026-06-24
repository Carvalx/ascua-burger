package com.carvalx.ascua_burger.pedido.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

// Lo que manda el cliente para añadir un producto al pedido
@Data
public class CrearLineaDto {

    @NotNull(message = "El producto es obligatorio")
    private UUID productoId;

    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;

    // Personalizaciones opcionales: {"sin_cebolla": true, "extra_queso": true}
    private Map<String, Object> personalizaciones;
}