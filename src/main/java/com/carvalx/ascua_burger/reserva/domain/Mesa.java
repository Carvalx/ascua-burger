package com.carvalx.ascua_burger.reserva.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "mesas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Número visible al cliente: "Mesa 3"
    @Column(nullable = false, unique = true)
    private Integer numero;

    // Capacidad máxima de comensales
    @Column(nullable = false)
    private Integer capacidad;

    // Zona del local: "Interior", "Terraza", "Barra"
    // Podrías hacer un enum si quisieras restringir los valores
    @Column
    private String zona;
}