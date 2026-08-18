package com.cermalagon.backend.controller;

import com.cermalagon.backend.dto.SolicitudesNoVistasDto;
import com.cermalagon.backend.service.SolicitudAdopcionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/solicitudes")
public class NotificacionesController {

    private final SolicitudAdopcionService solicitudAdopcionService;

    public NotificacionesController(SolicitudAdopcionService solicitudAdopcionService) {
        this.solicitudAdopcionService = solicitudAdopcionService;
    }

    // Requiere estar autenticada (ver SecurityConfig): qué gatos tienen solicitudes
    // que ninguna administradora ha visto todavía, para el aviso emergente.
    @GetMapping("/no-vistas")
    public SolicitudesNoVistasDto listarNoVistas() {
        return solicitudAdopcionService.listarNoVistas();
    }
}
