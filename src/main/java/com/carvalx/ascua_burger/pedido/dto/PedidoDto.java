package com.carvalx.ascua_burger.pedido.dto;

import com.carvalx.ascua_burger.pedido.domain.EstadoPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PedidoDto {
    private UUID id;
    private EstadoPedido estado;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<LineaPedidoDto> lineas;
}