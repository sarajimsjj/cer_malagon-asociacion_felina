package com.cermalagon.backend.dto;

import com.cermalagon.backend.entity.Administradora;

public record AdministradoraResumenDto(String nombreUsuario) {

    public static AdministradoraResumenDto desde(Administradora administradora) {
        return new AdministradoraResumenDto(administradora.getNombreUsuario());
    }
}
