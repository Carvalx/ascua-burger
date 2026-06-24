package com.carvalx.ascua_burger.producto.repository;

import com.carvalx.ascua_burger.producto.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    // Buscar por slug para URLs amigables: /api/categorias/clasicas
    Optional<Categoria> findBySlug(String slug);

    // Verificar si existe antes de crear para evitar duplicados
    boolean existsByNombre(String nombre);
}