package com.cermalagon.backend.controller;

import com.cermalagon.backend.dto.GatoFotoDto;
import com.cermalagon.backend.service.GatoFotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gatos/{gatoId}/fotos")
public class GatoFotoController {

    private final GatoFotoService gatoFotoService;

    public GatoFotoController(GatoFotoService gatoFotoService) {
        this.gatoFotoService = gatoFotoService;
    }

    // Público (ver SecurityConfig): la ficha de cada gato muestra todas sus fotos.
    @GetMapping
    public List<GatoFotoDto> listar(@PathVariable UUID gatoId) {
        return gatoFotoService.listarPorGato(gatoId);
    }

    // Requiere ser la administradora principal (ver SecurityConfig).
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GatoFotoDto> subir(
            @PathVariable UUID gatoId,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        GatoFotoDto creada = gatoFotoService.subir(gatoId, archivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // Requiere ser la administradora principal (ver SecurityConfig).
    @PatchMapping("/{fotoId}/principal")
    public GatoFotoDto marcarPrincipal(@PathVariable UUID gatoId, @PathVariable UUID fotoId) {
        return gatoFotoService.marcarPrincipal(gatoId, fotoId);
    }

    // Requiere ser la administradora principal (ver SecurityConfig).
    @DeleteMapping("/{fotoId}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID gatoId, @PathVariable UUID fotoId) {
        gatoFotoService.eliminar(gatoId, fotoId);
        return ResponseEntity.noContent().build();
    }
}
