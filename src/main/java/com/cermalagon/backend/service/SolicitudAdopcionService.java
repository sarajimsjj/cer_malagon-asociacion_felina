package com.cermalagon.backend.service;

import com.cermalagon.backend.dto.GatoConSolicitudesNoVistasDto;
import com.cermalagon.backend.dto.SolicitudAdopcionCreacionDto;
import com.cermalagon.backend.dto.SolicitudAdopcionResumenDto;
import com.cermalagon.backend.dto.SolicitudesNoVistasDto;
import com.cermalagon.backend.entity.EstadoGato;
import com.cermalagon.backend.entity.EstadoSolicitud;
import com.cermalagon.backend.entity.Gato;
import com.cermalagon.backend.entity.SolicitudAdopcion;
import com.cermalagon.backend.exception.GatoNoDisponibleException;
import com.cermalagon.backend.exception.RecursoNoEncontradoException;
import com.cermalagon.backend.repository.GatoRepository;
import com.cermalagon.backend.repository.SolicitudAdopcionRepository;
import com.cermalagon.backend.repository.SolicitudAdopcionRepository.ConteoNoVistasPorGato;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SolicitudAdopcionService {

    private static final Set<EstadoGato> ESTADOS_NO_DISPONIBLES = EnumSet.of(
            EstadoGato.ADOPTADO, EstadoGato.EN_TRATAMIENTO
    );

    private final SolicitudAdopcionRepository solicitudAdopcionRepository;
    private final GatoRepository gatoRepository;

    public SolicitudAdopcionService(
            SolicitudAdopcionRepository solicitudAdopcionRepository,
            GatoRepository gatoRepository
    ) {
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
        this.gatoRepository = gatoRepository;
    }

    public SolicitudAdopcionResumenDto crear(UUID gatoId, SolicitudAdopcionCreacionDto datos) {
        Gato gato = gatoRepository.findById(gatoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe ningún gato con id " + gatoId));

        if (ESTADOS_NO_DISPONIBLES.contains(gato.getEstado())) {
            throw new GatoNoDisponibleException("Este gato no está disponible para recibir solicitudes de adopción");
        }

        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setGatoId(gatoId);
        solicitud.setNombreSolicitante(datos.nombreSolicitante());
        solicitud.setEmail(datos.email());
        solicitud.setTelefono(datos.telefono());
        solicitud.setMensaje(datos.mensaje());

        SolicitudAdopcion guardada = solicitudAdopcionRepository.save(solicitud);
        return SolicitudAdopcionResumenDto.desde(guardada);
    }

    // Solo la llama una administradora autenticada (ver SecurityConfig).
    public List<SolicitudAdopcionResumenDto> listarPorGato(UUID gatoId) {
        comprobarQueElGatoExiste(gatoId);

        List<SolicitudAdopcion> solicitudes = solicitudAdopcionRepository.findByGatoIdOrderByFechaCreacionDesc(gatoId);

        // Al abrir la lista de un gato, sus solicitudes dejan de contar como notificación pendiente.
        List<SolicitudAdopcion> noVistas = solicitudes.stream().filter(s -> !s.isVista()).toList();
        if (!noVistas.isEmpty()) {
            noVistas.forEach(s -> s.setVista(true));
            solicitudAdopcionRepository.saveAll(noVistas);
        }

        return solicitudes.stream()
                .map(SolicitudAdopcionResumenDto::desde)
                .toList();
    }

    // Solo la llama una administradora autenticada (ver SecurityConfig): qué gatos tienen
    // solicitudes que ninguna administradora ha visto todavía, para el aviso emergente.
    public SolicitudesNoVistasDto listarNoVistas() {
        List<ConteoNoVistasPorGato> conteos = solicitudAdopcionRepository.contarNoVistasAgrupadoPorGato();
        if (conteos.isEmpty()) {
            return new SolicitudesNoVistasDto(0, List.of());
        }

        Map<UUID, String> nombresPorGato = gatoRepository
                .findAllById(conteos.stream().map(ConteoNoVistasPorGato::getGatoId).toList())
                .stream()
                .collect(Collectors.toMap(Gato::getId, Gato::getNombre));

        List<GatoConSolicitudesNoVistasDto> gatos = conteos.stream()
                .map(c -> new GatoConSolicitudesNoVistasDto(
                        c.getGatoId(),
                        nombresPorGato.getOrDefault(c.getGatoId(), "Gato eliminado"),
                        c.getCantidad()
                ))
                .toList();

        long total = gatos.stream().mapToLong(GatoConSolicitudesNoVistasDto::cantidad).sum();
        return new SolicitudesNoVistasDto(total, gatos);
    }

    // Solo la llama una administradora autenticada (ver SecurityConfig).
    public SolicitudAdopcionResumenDto cambiarEstado(UUID gatoId, UUID solicitudId, EstadoSolicitud nuevoEstado) {
        SolicitudAdopcion solicitud = solicitudAdopcionRepository.findByIdAndGatoId(solicitudId, gatoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe ninguna solicitud con id " + solicitudId + " para ese gato"));

        solicitud.setEstado(nuevoEstado);
        SolicitudAdopcion guardada = solicitudAdopcionRepository.save(solicitud);
        return SolicitudAdopcionResumenDto.desde(guardada);
    }

    private void comprobarQueElGatoExiste(UUID gatoId) {
        if (!gatoRepository.existsById(gatoId)) {
            throw new RecursoNoEncontradoException("No existe ningún gato con id " + gatoId);
        }
    }
}
