package com.cermalagon.backend.dto;

import java.util.List;

public record SolicitudesNoVistasDto(long total, List<GatoConSolicitudesNoVistasDto> gatos) {
}
