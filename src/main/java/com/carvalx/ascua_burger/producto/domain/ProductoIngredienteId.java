package com.carvalx.ascua_burger.producto.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

// Clave primaria compuesta: (producto_id, ingrediente_id)
// Debe implementar Serializable — requisito de JPA para PKs embebidas
// Debe tener equals() y hashCode() correctos — Lombok @EqualsAndHashCode lo hace
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductoIngredienteId implements Serializable {
    private UUID productoId;
    private UUID ingredienteId;
}