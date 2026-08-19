package com.cermalagon.backend.controller;

import com.cermalagon.backend.dto.ComentarioSolicitudCreacionDto;
import com.cermalagon.backend.dto.ComentarioSolicitudDto;
import com.cermalagon.backend.service.ComentarioSolicitudService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gatos/{gatoId}/solicitudes/{solicitudId}/comentarios")
public class ComentarioSolicitudController {

    private final ComentarioSolicitudService comentarioSolicitudService;

    public ComentarioSolicitudController(ComentarioSolicitudService comentarioSolicitudService) {
        this.comentarioSolicitudService = comentarioSolicitudService;
    }

    // Requiere estar autenticada (ver SecurityConfig): son notas internas de las administradoras.
    @GetMapping
    public List<ComentarioSolicitudDto> listar(@PathVariable UUID gatoId, @PathVariable UUID solicitudId) {
        return comentarioSolicitudService.listarPorSolicitud(gatoId, solicitudId);
    }

    // Requiere estar autenticada (ver SecurityConfig).
    @PostMapping
    public ResponseEntity<ComentarioSolicitudDto> crear(
            @PathVariable UUID gatoId,
            @PathVariable UUID solicitudId,
            @Valid @RequestBody ComentarioSolicitudCreacionDto datos,
            Authentication autenticacion
    ) {
        ComentarioSolicitudDto creado = comentarioSolicitudService.crear(
                gatoId, solicitudId, autenticacion.getName(), datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}
