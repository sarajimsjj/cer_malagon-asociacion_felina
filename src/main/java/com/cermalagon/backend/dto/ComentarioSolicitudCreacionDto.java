package com.cermalagon.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ComentarioSolicitudCreacionDto(
        @NotBlank(message = "El comentario no puede estar vacío")
        @Size(max = 2000, message = "El comentario no puede superar los 2000 caracteres")
        String texto
) {
}
