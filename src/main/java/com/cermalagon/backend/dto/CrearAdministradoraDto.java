package com.cermalagon.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearAdministradoraDto(
        @NotBlank(message = "El usuario es obligatorio")
        @Size(min = 3, max = 100, message = "El usuario debe tener entre 3 y 100 caracteres")
        String nombreUsuario,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String contrasena
) {
}
