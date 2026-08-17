package com.cermalagon.backend.dto;

import com.cermalagon.backend.entity.GatoFoto;
import com.cermalagon.backend.entity.TipoMedia;

import java.util.UUID;

public record GatoFotoDto(UUID id, String url, boolean esPrincipal, TipoMedia tipo) {

    public static GatoFotoDto desde(GatoFoto foto) {
        return new GatoFotoDto(foto.getId(), foto.getUrl(), foto.isEsPrincipal(), foto.getTipo());
    }
}
