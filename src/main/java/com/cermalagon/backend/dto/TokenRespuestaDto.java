package com.cermalagon.backend.dto;

import com.cermalagon.backend.entity.RolAdministradora;

public record TokenRespuestaDto(String token, String nombreUsuario, RolAdministradora rol) {
}
