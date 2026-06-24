package com.carvalx.ascua_burger.producto.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ingredientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    // Precio adicional si el cliente añade este ingrediente extra
    // BigDecimal para dinero — nunca uses double/float para precios
    // porque tienen errores de precisión: 0.1 + 0.2 = 0.30000000000000004
    @Column(name = "precio_extra", nullable = false, precision = 6, scale = 2)
    @Builder.Default
    private BigDecimal precioExtra = BigDecimal.ZERO;

    // Stock actual en cocina — podrías hacer un endpoint para que
    // cocina actualice esto en tiempo real
    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    // Marca si es alérgeno (gluten, lácteo, fruto seco...)
    // Podrías extenderlo con un enum TipoAlergeno si quisieras
    // cumplir la normativa EU de alérgenos en carta digital
    @Column(name = "es_alergeno", nullable = false)
    @Builder.Default
    private Boolean esAlergeno = false;
}