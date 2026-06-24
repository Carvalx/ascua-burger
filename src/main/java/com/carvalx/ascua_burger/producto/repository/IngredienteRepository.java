package com.carvalx.ascua_burger.producto.repository;

import com.carvalx.ascua_burger.producto.domain.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IngredienteRepository extends JpaRepository<Ingrediente, UUID> {

    // Útil para el panel de admin: listar solo los alérgenos
    List<Ingrediente> findByEsAlergenoTrue();

    // Útil para cocina: ver qué ingredientes están bajo de stock
    // Podrías crear un endpoint /api/ingredientes/bajo-stock?umbral=5
    List<Ingrediente> findByStockLessThan(Integer umbral);
}