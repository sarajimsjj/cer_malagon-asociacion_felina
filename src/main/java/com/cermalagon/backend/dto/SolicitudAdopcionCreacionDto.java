package com.cermalagon.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SolicitudAdopcionCreacionDto(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String nombreSolicitante,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no es válido")
        @Size(max = 150, message = "El email no puede superar los 150 caracteres")
        String email,

        @Size(max = 30, message = "El teléfono no puede superar los 30 caracteres")
        String telefono,

        String mensaje
) {
}
