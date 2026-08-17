package com.cermalagon.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank(message = "El usuario es obligatorio")
        String nombreUsuario,

        @NotBlank(message = "La contraseña es obligatoria")
        String contrasena
) {
}
