package com.cermalagon.backend.service;

import com.cermalagon.backend.dto.ComentarioSolicitudCreacionDto;
import com.cermalagon.backend.dto.ComentarioSolicitudDto;
import com.cermalagon.backend.entity.ComentarioSolicitud;
import com.cermalagon.backend.exception.RecursoNoEncontradoException;
import com.cermalagon.backend.repository.ComentarioSolicitudRepository;
import com.cermalagon.backend.repository.SolicitudAdopcionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ComentarioSolicitudService {

    private final ComentarioSolicitudRepository comentarioSolicitudRepository;
    private final SolicitudAdopcionRepository solicitudAdopcionRepository;

    public ComentarioSolicitudService(
            ComentarioSolicitudRepository comentarioSolicitudRepository,
            SolicitudAdopcionRepository solicitudAdopcionRepository
    ) {
        this.comentarioSolicitudRepository = comentarioSolicitudRepository;
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
    }

    // Solo la llama una administradora autenticada (ver SecurityConfig).
    public List<ComentarioSolicitudDto> listarPorSolicitud(UUID gatoId, UUID solicitudId) {
        comprobarQueLaSolicitudExiste(gatoId, solicitudId);

        return comentarioSolicitudRepository.findBySolicitudIdOrderByFechaCreacionAsc(solicitudId)
                .stream()
                .map(ComentarioSolicitudDto::desde)
                .toList();
    }

    // Solo la llama una administradora autenticada (ver SecurityConfig); "autora" es el
    // nombre de usuario que llega del token, no algo que decida quien llama al endpoint.
    public ComentarioSolicitudDto crear(UUID gatoId, UUID solicitudId, String autora, ComentarioSolicitudCreacionDto datos) {
        comprobarQueLaSolicitudExiste(gatoId, solicitudId);

        ComentarioSolicitud comentario = new ComentarioSolicitud();
        comentario.setSolicitudId(solicitudId);
        comentario.setAutora(autora);
        comentario.setTexto(datos.texto());

        ComentarioSolicitud guardado = comentarioSolicitudRepository.save(comentario);
        return ComentarioSolicitudDto.desde(guardado);
    }

    private void comprobarQueLaSolicitudExiste(UUID gatoId, UUID solicitudId) {
        solicitudAdopcionRepository.findByIdAndGatoId(solicitudId, gatoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe ninguna solicitud con id " + solicitudId + " para ese gato"));
    }
}
