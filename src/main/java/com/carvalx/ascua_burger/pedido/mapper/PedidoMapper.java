package com.carvalx.ascua_burger.pedido.mapper;

import com.carvalx.ascua_burger.pedido.domain.LineaPedido;
import com.carvalx.ascua_burger.pedido.domain.Pedido;
import com.carvalx.ascua_burger.pedido.dto.LineaPedidoDto;
import com.carvalx.ascua_burger.pedido.dto.PedidoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    @Mapping(target = "lineas", source = "lineas")
    PedidoDto toDto(Pedido pedido);

    // Mapeamos producto.id y producto.nombre desde la relación
    // subtotal lo calculamos aquí mismo con una expresión
    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "productoNombre", source = "producto.nombre")
    @Mapping(target = "subtotal",
            expression = "java(lineaPedido.getPrecioUnitario().multiply(java.math.BigDecimal.valueOf(lineaPedido.getCantidad())))")
    LineaPedidoDto toDto(LineaPedido lineaPedido);
}