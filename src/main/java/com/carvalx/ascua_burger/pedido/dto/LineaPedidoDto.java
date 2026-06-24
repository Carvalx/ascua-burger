package com.carvalx.ascua_burger.pedido.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
public class LineaPedidoDto {
    private UUID id;
    private UUID productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    // Total de esta línea: precioUnitario * cantidad
    // Lo calculamos en el mapper para no tener lógica en el frontend
    private BigDecimal subtotal;
    private Map<String, Object> personalizaciones;
}