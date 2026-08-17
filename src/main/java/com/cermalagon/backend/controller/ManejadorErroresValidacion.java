package com.cermalagon.backend.controller;

import com.cermalagon.backend.exception.CredencialesInvalidasException;
import com.cermalagon.backend.exception.GatoNoDisponibleException;
import com.cermalagon.backend.exception.NombreUsuarioDuplicadoException;
import com.cermalagon.backend.exception.RecursoNoEncontradoException;
import com.cermalagon.backend.exception.TipoArchivoNoPermitidoException;
import com.cermalagon.backend.exception.VideoNoPuedeSerPrincipalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorErroresValidacion {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<Map<String, String>> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(NombreUsuarioDuplicadoException.class)
    public ResponseEntity<Map<String, String>> manejarUsuarioDuplicado(NombreUsuarioDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(GatoNoDisponibleException.class)
    public ResponseEntity<Map<String, String>> manejarGatoNoDisponible(GatoNoDisponibleException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(TipoArchivoNoPermitidoException.class)
    public ResponseEntity<Map<String, String>> manejarTipoArchivoNoPermitido(TipoArchivoNoPermitidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(VideoNoPuedeSerPrincipalException.class)
    public ResponseEntity<Map<String, String>> manejarVideoNoPuedeSerPrincipal(VideoNoPuedeSerPrincipalException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> manejarArchivoDemasiadoGrande(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Map.of("error", "El archivo es demasiado grande (máximo 50 MB)"));
    }
}