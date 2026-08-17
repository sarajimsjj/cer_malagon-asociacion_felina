package com.cermalagon.backend.dto;

import com.cermalagon.backend.entity.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;

public record CambioEstadoSolicitudDto(
        @NotNull(message = "El estado es obligatorio")
        EstadoSolicitud estado
) {
}
