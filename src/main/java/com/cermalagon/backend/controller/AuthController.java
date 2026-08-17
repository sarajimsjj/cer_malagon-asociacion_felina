package com.cermalagon.backend.controller;

import com.cermalagon.backend.dto.AdministradoraResumenDto;
import com.cermalagon.backend.dto.CrearAdministradoraDto;
import com.cermalagon.backend.dto.LoginDto;
import com.cermalagon.backend.dto.TokenRespuestaDto;
import com.cermalagon.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public TokenRespuestaDto login(@Valid @RequestBody LoginDto datos) {
        return authService.login(datos);
    }

    // Requiere estar autenticada (ver SecurityConfig): solo una administradora ya
    // registrada puede invitar a otra, no hay alta pública.
    @PostMapping("/administradoras")
    public ResponseEntity<AdministradoraResumenDto> crearAdministradora(@Valid @RequestBody CrearAdministradoraDto datos) {
        AdministradoraResumenDto creada = authService.crearAdministradora(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }
}
