package com.cermalagon.backend.dto;

import com.cermalagon.backend.entity.ComentarioSolicitud;

import java.time.LocalDateTime;
import java.util.UUID;

public record ComentarioSolicitudDto(
        UUID id,
        UUID solicitudId,
        String autora,
        String texto,
        LocalDateTime fechaCreacion
) {
    public static ComentarioSolicitudDto desde(ComentarioSolicitud comentario) {
        return new ComentarioSolicitudDto(
                comentario.getId(),
                comentario.getSolicitudId(),
                comentario.getAutora(),
                comentario.getTexto(),
                comentario.getFechaCreacion()
        );
    }
}
