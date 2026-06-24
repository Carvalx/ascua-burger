package com.carvalx.ascua_burger.reserva.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class MesaDto {
    private UUID id;
    private Integer numero;
    private Integer capacidad;
    private String zona;
}