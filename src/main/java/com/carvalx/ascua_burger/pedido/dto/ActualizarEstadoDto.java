package com.carvalx.ascua_burger.pedido.dto;

import com.carvalx.ascua_burger.pedido.domain.EstadoPedido;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// Solo el rol COCINA o ADMIN puede cambiar el estado
// CLIENTE solo puede cancelar su propio pedido
@Data
public class ActualizarEstadoDto {

    @NotNull(message = "El estado es obligatorio")
    private EstadoPedido estado;
}