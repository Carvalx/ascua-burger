package com.carvalx.ascua_burger.producto.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    // Descripción larga para la carta — puede contener HTML si quisieras
    // renderizarlo en el frontend con dangerouslySetInnerHTML en React
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Precio base sin extras — BigDecimal siempre para dinero
    @Column(name = "precio_base", nullable = false, precision = 8, scale = 2)
    private BigDecimal precioBase;

    // ManyToOne: muchos productos pertenecen a una categoría
    // FetchType.LAZY = no carga la categoría hasta que la necesitas
    // FetchType.EAGER (el default) cargaría la categoría en cada query de producto
    // LAZY es casi siempre la mejor opción para evitar queries innecesarias
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Si false, no aparece en la carta — útil para productos de temporada
    // o cuando se acaba un ingrediente principal sin tener que borrarlo
    @Column(nullable = false)
    @Builder.Default
    private Boolean disponible = true;

    // URL de la imagen — en producción apuntaría a Cloudinary o S3
    // Para dev puedes usar imágenes de Unsplash con URL directa
    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relación con los ingredientes a través de la tabla intermedia
    // mappedBy = "producto" porque ProductoIngrediente tiene el campo producto
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductoIngrediente> ingredientes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}