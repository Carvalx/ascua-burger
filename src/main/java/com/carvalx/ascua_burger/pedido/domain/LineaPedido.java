package com.carvalx.ascua_burger.pedido.domain;

import com.carvalx.ascua_burger.producto.domain.Producto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "lineas_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineaPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // ManyToOne LAZY: no cargamos el pedido completo cada vez que cargamos una línea
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    // Guardamos el precio en el momento del pedido — no el precio actual
    // Si mañana sube el precio de la hamburguesa, los pedidos anteriores
    // deben mantener el precio original. Esto es crítico en e-commerce.
    @Column(name = "precio_unitario", nullable = false, precision = 8, scale = 2)
    private BigDecimal precioUnitario;

    // JSONB para personalizaciones: {"sin_cebolla": true, "extra_queso": true}
    // No normalizamos porque son datos de solo lectura que varían por pedido
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> personalizaciones;
}