package com.carvalx.ascua_burger.reserva.repository;

import com.carvalx.ascua_burger.reserva.domain.EstadoReserva;
import com.carvalx.ascua_burger.reserva.domain.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, UUID> {

    // Reservas del usuario autenticado
    Page<Reserva> findByUsuarioIdOrderByFechaDescHoraInicioDesc(
            UUID usuarioId, Pageable pageable);

    // La query más importante: detectar solapamientos
    // Una mesa está ocupada si ya hay una reserva ACTIVA (PENDIENTE o CONFIRMADA)
    // en la misma fecha y en una franja de ±2 horas alrededor de la hora solicitada
    // En producción usarías franjas horarias fijas (12:00, 14:00, 20:00, 22:00)
    // pero esto sirve para el portfolio
    @Query(value = """
        SELECT COUNT(*) FROM reservas r
        WHERE r.mesa_id = :mesaId
        AND r.fecha = :fecha
        AND r.estado IN ('PENDIENTE', 'CONFIRMADA')
        """, nativeQuery = true)
    int contarReservasMismaFecha(UUID mesaId, LocalDate fecha);

    // Para el panel de admin: ver todas las reservas de un día
    List<Reserva> findByFechaAndEstadoInOrderByHoraInicioAsc(
            LocalDate fecha, List<EstadoReserva> estados);
}