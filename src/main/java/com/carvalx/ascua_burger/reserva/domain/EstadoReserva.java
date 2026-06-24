package com.carvalx.ascua_burger.reserva.domain;

// Flujo: PENDIENTE → CONFIRMADA → COMPLETADA
// CANCELADA puede venir desde PENDIENTE o CONFIRMADA
// Una reserva COMPLETADA es cuando el cliente ya vino y se fue
public enum EstadoReserva {
    PENDIENTE,
    CONFIRMADA,
    CANCELADA,
    COMPLETADA
}