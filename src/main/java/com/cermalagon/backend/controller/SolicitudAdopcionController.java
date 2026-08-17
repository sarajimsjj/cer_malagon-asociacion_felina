package com.cermalagon.backend.controller;

import com.cermalagon.backend.dto.CambioEstadoSolicitudDto;
import com.cermalagon.backend.dto.SolicitudAdopcionCreacionDto;
import com.cermalagon.backend.dto.SolicitudAdopcionResumenDto;
import com.cermalagon.backend.service.SolicitudAdopcionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gatos/{gatoId}/solicitudes")
public class SolicitudAdopcionController {

    private final SolicitudAdopcionService solicitudAdopcionService;

    public SolicitudAdopcionController(SolicitudAdopcionService solicitudAdopcionService) {
        this.solicitudAdopcionService = solicitudAdopcionService;
    }

    // Público (ver SecurityConfig): cualquier visitante puede solicitar adoptar un gato.
    @PostMapping
    public ResponseEntity<SolicitudAdopcionResumenDto> crear(
            @PathVariable UUID gatoId,
            @Valid @RequestBody SolicitudAdopcionCreacionDto datos
    ) {
        SolicitudAdopcionResumenDto creada = solicitudAdopcionService.crear(gatoId, datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // Requiere estar autenticada: las solicitudes contienen datos personales de terceros.
    @GetMapping
    public List<SolicitudAdopcionResumenDto> listar(@PathVariable UUID gatoId) {
        return solicitudAdopcionService.listarPorGato(gatoId);
    }

    // Requiere estar autenticada: solo una administradora decide el estado de una solicitud.
    @PatchMapping("/{id}/estado")
    public SolicitudAdopcionResumenDto cambiarEstado(
            @PathVariable UUID gatoId,
            @PathVariable UUID id,
            @Valid @RequestBody CambioEstadoSolicitudDto datos
    ) {
        return solicitudAdopcionService.cambiarEstado(gatoId, id, datos.estado());
    }
}
