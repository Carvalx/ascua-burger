package com.carvalx.ascua_burger.producto.repository;

import com.carvalx.ascua_burger.producto.domain.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductoRepository extends JpaRepository<Producto, UUID> {

    // Paginación automática — Spring Data genera el SQL con LIMIT/OFFSET
    // El cliente manda: GET /api/productos?page=0&size=6&sort=nombre,asc
    Page<Producto> findByDisponibleTrue(Pageable pageable);

    // Filtrar por categoría con paginación
    // Sirve para el botón de filtro en la carta: "CLÁSICAS", "PICANTES"...
    Page<Producto> findByCategoriaIdAndDisponibleTrue(UUID categoriaId, Pageable pageable);

    // JOIN FETCH: carga el producto Y su categoría en una sola query
    // Sin esto, si cargas 10 productos Hibernate haría 1 query para los productos
    // + 10 queries para cada categoría = problema N+1
    // Con JOIN FETCH hace 1 sola query con un JOIN
    @Query("SELECT p FROM Producto p JOIN FETCH p.categoria WHERE p.id = :id")
    Optional<Producto> findByIdWithCategoria(UUID id);

    // Para el buscador del frontend: busca por nombre ignorando mayúsculas
    // LIKE %nombre% es lento en tablas grandes — en producción usarías
    // PostgreSQL full-text search o Elasticsearch
    Page<Producto> findByNombreContainingIgnoreCaseAndDisponibleTrue(
            String nombre, Pageable pageable);
}