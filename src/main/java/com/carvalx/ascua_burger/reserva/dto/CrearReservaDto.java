package com.carvalx.ascua_burger.reserva.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class CrearReservaDto {

    @NotNull(message = "La mesa es obligatoria")
    private UUID mesaId;

    @NotNull(message = "La fecha es obligatoria")
    @Future(message = "La fecha debe ser futura")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime horaInicio;

    @Min(value = 1, message = "Mínimo 1 comensal")
    @Max(value = 20, message = "Máximo 20 comensales")
    private Integer comensales;
}