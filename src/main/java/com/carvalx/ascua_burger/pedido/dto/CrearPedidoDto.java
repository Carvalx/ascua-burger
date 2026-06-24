package com.carvalx.ascua_burger.pedido.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CrearPedidoDto {

    @NotEmpty(message = "El pedido debe tener al menos una línea")
    private List<CrearLineaDto> lineas;
}