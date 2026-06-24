package com.carvalx.ascua_burger.producto.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

// Esta clase mapea la tabla producto_ingredientes
// Es la forma profesional de manejar ManyToMany cuando necesitas
// datos extra en la tabla intermedia (como incluidoPorDefecto)
// Si solo necesitaras la relación sin datos extra, usarías
// @ManyToMany directamente en Producto — pero así tienes más control
@Entity
@Table(name = "producto_ingredientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoIngrediente {

    @EmbeddedId
    private ProductoIngredienteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productoId")
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredienteId")
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    // Si es true, el ingrediente viene en la hamburguesa por defecto
    // Si es false, es un extra opcional que el cliente puede añadir
    @Column(name = "incluido_por_defecto", nullable = false)
    @Builder.Default
    private Boolean incluidoPorDefecto = true;
}