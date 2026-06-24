package com.carvalx.ascua_burger.producto.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // El nombre visible al cliente: "Clásicas", "Picantes", "Veganas"
    @Column(nullable = false)
    private String nombre;

    // Slug para URLs amigables: "clasicas", "picantes"
    // Si quisieras una web pública, usarías /carta/clasicas en vez de /carta/uuid
    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Una categoría tiene muchos productos
    // mappedBy = "categoria" indica que la FK vive en la tabla productos, no aquí
    // cascade = PERSIST significa que si guardas una categoría con productos nuevos, los guarda también
    // orphanRemoval = true significa que si quitas un producto de esta lista, se borra de BD
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.PERSIST, orphanRemoval = false)
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // Auto-generamos el slug si no viene informado
        // En producción usarías una librería como slugify
        if (slug == null && nombre != null) {
            slug = nombre.toLowerCase()
                    .replace(" ", "-")
                    .replace("á", "a").replace("é", "e")
                    .replace("í", "i").replace("ó", "o")
                    .replace("ú", "u");
        }
    }
}