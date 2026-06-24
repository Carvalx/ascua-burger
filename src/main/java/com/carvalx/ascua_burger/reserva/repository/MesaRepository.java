package com.carvalx.ascua_burger.reserva.repository;

import com.carvalx.ascua_burger.reserva.domain.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MesaRepository extends JpaRepository<Mesa, UUID> {

    // Buscar mesas con capacidad suficiente para los comensales
    // El cliente pone "somos 4" y solo ve mesas de 4 o más
    List<Mesa> findByCapacidadGreaterThanEqual(Integer capacidad);
}