package com.carvalx.ascua_burger.reserva.controller;

import com.carvalx.ascua_burger.reserva.dto.CrearReservaDto;
import com.carvalx.ascua_burger.reserva.dto.MesaDto;
import com.carvalx.ascua_burger.reserva.dto.ReservaDto;
import com.carvalx.ascua_burger.reserva.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    // Mesas disponibles — público para que el cliente vea antes de registrarse
    // GET /api/reservas/disponibilidad?fecha=2026-06-25&hora=20:00&comensales=2
    @GetMapping("/disponibilidad")
    public ResponseEntity<List<MesaDto>> mesasDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
            @RequestParam Integer comensales) {
        return ResponseEntity.ok(
                reservaService.mesasDisponibles(fecha, hora, comensales));
    }

    // Crear reserva — usuario autenticado
    @PostMapping
    public ResponseEntity<ReservaDto> crear(
            @Valid @RequestBody CrearReservaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservaService.crear(dto));
    }

    // Mis reservas
    @GetMapping("/mis-reservas")
    public ResponseEntity<Page<ReservaDto>> misReservas(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(reservaService.misReservas(pageable));
    }

    // Cancelar reserva propia
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaDto> cancelar(@PathVariable UUID id) {
        return ResponseEntity.ok(reservaService.cancelar(id));
    }

    // Reservas del día — solo ADMIN
    @GetMapping("/dia")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservaDto>> reservasDelDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {
        return ResponseEntity.ok(reservaService.reservasDelDia(fecha));
    }
}