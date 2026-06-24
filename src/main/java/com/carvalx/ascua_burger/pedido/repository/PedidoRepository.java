package com.carvalx.ascua_burger.pedido.repository;

import com.carvalx.ascua_burger.pedido.domain.EstadoPedido;
import com.carvalx.ascua_burger.pedido.domain.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    // Pedidos del usuario autenticado — para "mis pedidos"
    Page<Pedido> findByUsuarioIdOrderByCreatedAtDesc(UUID usuarioId, Pageable pageable);

    // JOIN FETCH para cargar pedido + líneas en una sola query
    // Sin esto, al acceder a pedido.getLineas() haría una query extra por cada pedido
    @Query("SELECT p FROM Pedido p JOIN FETCH p.lineas WHERE p.id = :id")
    Optional<Pedido> findByIdWithLineas(UUID id);

    // Para el panel de cocina: ver pedidos pendientes por estado
    List<Pedido> findByEstadoOrderByCreatedAtAsc(EstadoPedido estado);
}