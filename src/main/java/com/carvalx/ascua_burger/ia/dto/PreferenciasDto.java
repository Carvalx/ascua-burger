package com.carvalx.ascua_burger.ia.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PreferenciasDto {

    @NotNull(message = "El presupuesto es obligatorio")
    @Min(value = 1, message = "El presupuesto mínimo es 1€")
    @Max(value = 100, message = "El presupuesto máximo es 100€")
    private Double presupuesto;

    @NotNull(message = "El número de comensales es obligatorio")
    @Min(value = 1)
    @Max(value = 10)
    private Integer comensales;

    // Texto libre: "me gusta picante, sin lácteos, algo contundente"
    private String preferencias;
}