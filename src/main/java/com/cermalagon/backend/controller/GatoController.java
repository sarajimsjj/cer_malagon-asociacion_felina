package com.cermalagon.backend.controller;


import com.cermalagon.backend.dto.GatoCreacionDto;
import com.cermalagon.backend.dto.GatoResumenDto;
import com.cermalagon.backend.service.GatoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/gatos")
public class GatoController {
    private final GatoService gatoService;

    public GatoController(GatoService gatoService) {
        this.gatoService = gatoService;
    }

    @GetMapping
    public List<GatoResumenDto> listarGatos() {
        return gatoService.listarTodos();
    }

    @GetMapping("/{id}")
    public GatoResumenDto obtenerGato(@PathVariable UUID id) {
        return gatoService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<GatoResumenDto> crearGato(@Valid @RequestBody GatoCreacionDto datos){
        GatoResumenDto creado = gatoService.crear(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Requiere estar autenticada (ver SecurityConfig): solo una administradora puede editar un gato.
    @PutMapping("/{id}")
    public GatoResumenDto actualizarGato(@PathVariable UUID id, @Valid @RequestBody GatoCreacionDto datos) {
        return gatoService.actualizar(id, datos);
    }
}
