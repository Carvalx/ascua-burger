package com.carvalx.ascua_burger.pedido.domain;

import com.carvalx.ascua_burger.usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // @Enumerated(EnumType.STRING) con NAMED_ENUM para PostgreSQL
    // igual que hicimos con Rol en Usuario
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "estado_pedido")
    @Builder.Default
    private EstadoPedido estado = EstadoPedido.RECIBIDO;

    // El total se calcula en el service al confirmar el pedido
    // sum(linea.precioUnitario * linea.cantidad)
    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // CascadeType.ALL: al guardar el pedido se guardan las líneas también
    // orphanRemoval = true: si quitas una línea del pedido se borra de BD
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LineaPedido> lineas = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Método de dominio: añadir una línea y recalcular el total
    // La lógica de negocio vive en la entidad, no dispersa por el service
    // Esto se llama "rich domain model" — patrón DDD básico
    public void agregarLinea(LineaPedido linea) {
        linea.setPedido(this);
        lineas.add(linea);
        recalcularTotal();
    }

    public void recalcularTotal() {
        this.total = lineas.stream()
                .map(l -> l.getPrecioUnitario().multiply(BigDecimal.valueOf(l.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}