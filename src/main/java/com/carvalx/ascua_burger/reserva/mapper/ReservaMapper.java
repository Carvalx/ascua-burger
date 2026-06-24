package com.carvalx.ascua_burger.reserva.mapper;

import com.carvalx.ascua_burger.reserva.domain.Mesa;
import com.carvalx.ascua_burger.reserva.domain.Reserva;
import com.carvalx.ascua_burger.reserva.dto.MesaDto;
import com.carvalx.ascua_burger.reserva.dto.ReservaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    MesaDto toDto(Mesa mesa);

    ReservaDto toDto(Reserva reserva);
}