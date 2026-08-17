package com.cermalagon.backend.dto;

import com.cermalagon.backend.entity.EstadoSolicitud;
import com.cermalagon.backend.entity.SolicitudAdopcion;

import java.time.LocalDateTime;
import java.util.UUID;

public record SolicitudAdopcionResumenDto(
        UUID id,
        UUID gatoId,
        String nombreSolicitante,
        String email,
        String telefono,
        String mensaje,
        EstadoSolicitud estado,
        LocalDateTime fechaCreacion
) {
    public static SolicitudAdopcionResumenDto desde(SolicitudAdopcion solicitud) {
        return new SolicitudAdopcionResumenDto(
                solicitud.getId(),
                solicitud.getGatoId(),
                solicitud.getNombreSolicitante(),
                solicitud.getEmail(),
                solicitud.getTelefono(),
                solicitud.getMensaje(),
                solicitud.getEstado(),
                solicitud.getFechaCreacion()
        );
    }
}
