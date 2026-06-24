package com.carvalx.ascua_burger.pedido.domain;

// Máquina de estados del pedido
// El flujo normal es: RECIBIDO → EN_PREPARACION → LISTO → ENTREGADO
// CANCELADO puede venir desde RECIBIDO o EN_PREPARACION
// En la entrevista: "¿cómo controlas las transiciones?"
// Respuesta: en el service valido que solo se pueda avanzar al siguiente estado,
// no saltar ni retroceder (excepto CANCELADO)
public enum EstadoPedido {
    RECIBIDO,
    EN_PREPARACION,
    LISTO,
    ENTREGADO,
    CANCELADO
}