package com.carvalx.ascua_burger.reserva.dto;

import com.carvalx.ascua_burger.reserva.domain.EstadoReserva;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class ReservaDto {
    private UUID id;
    private MesaDto mesa;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private Integer comensales;
    private EstadoReserva estado;
    private LocalDateTime createdAt;
}