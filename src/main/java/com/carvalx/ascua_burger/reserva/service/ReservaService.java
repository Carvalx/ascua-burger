package com.carvalx.ascua_burger.reserva.service;

import com.carvalx.ascua_burger.reserva.domain.EstadoReserva;
import com.carvalx.ascua_burger.reserva.domain.Mesa;
import com.carvalx.ascua_burger.reserva.domain.Reserva;
import com.carvalx.ascua_burger.reserva.dto.CrearReservaDto;
import com.carvalx.ascua_burger.reserva.dto.MesaDto;
import com.carvalx.ascua_burger.reserva.dto.ReservaDto;
import com.carvalx.ascua_burger.reserva.mapper.ReservaMapper;
import com.carvalx.ascua_burger.reserva.repository.MesaRepository;
import com.carvalx.ascua_burger.reserva.repository.ReservaRepository;
import com.carvalx.ascua_burger.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final MesaRepository mesaRepository;
    private final ReservaMapper reservaMapper;

    @Transactional(readOnly = true)
    public List<MesaDto> mesasDisponibles(LocalDate fecha,
                                          LocalTime hora,
                                          Integer comensales) {
        var mesas = mesaRepository.findByCapacidadGreaterThanEqual(comensales);

        return mesas.stream()
                .filter(mesa -> {
                    int reservas = reservaRepository
                            .contarReservasMismaFecha(mesa.getId(), fecha);
                    return reservas == 0;
                })
                .map(reservaMapper::toDto)
                .toList();
    }

    @Transactional
    public ReservaDto crear(CrearReservaDto dto) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Mesa mesa = mesaRepository.findById(dto.getMesaId())
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        if (mesa.getCapacidad() < dto.getComensales()) {
            throw new RuntimeException(
                    "La mesa " + mesa.getNumero() +
                            " solo tiene capacidad para " + mesa.getCapacidad() +
                            " comensales");
        }

        if (reservaRepository.contarReservasMismaFecha(
                mesa.getId(), dto.getFecha()) > 0) {
            throw new RuntimeException(
                    "La mesa " + mesa.getNumero() +
                            " no está disponible en esa fecha");
        }

        Reserva reserva = Reserva.builder()
                .usuario(usuario)
                .mesa(mesa)
                .fecha(dto.getFecha())
                .horaInicio(dto.getHoraInicio())
                .comensales(dto.getComensales())
                .build();

        return reservaMapper.toDto(reservaRepository.save(reserva));
    }

    @Transactional(readOnly = true)
    public Page<ReservaDto> misReservas(Pageable pageable) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return reservaRepository
                .findByUsuarioIdOrderByFechaDescHoraInicioDesc(
                        usuario.getId(), pageable)
                .map(reservaMapper::toDto);
    }

    @Transactional
    public ReservaDto cancelar(UUID id) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!reserva.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No puedes cancelar una reserva que no es tuya");
        }

        if (reserva.getEstado() == EstadoReserva.COMPLETADA) {
            throw new RuntimeException("No se puede cancelar una reserva completada");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaMapper.toDto(reservaRepository.save(reserva));
    }

    @Transactional(readOnly = true)
    public List<ReservaDto> reservasDelDia(LocalDate fecha) {
        return reservaRepository.findByFechaAndEstadoInOrderByHoraInicioAsc(
                        fecha,
                        List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA))
                .stream()
                .map(reservaMapper::toDto)
                .toList();
    }
}