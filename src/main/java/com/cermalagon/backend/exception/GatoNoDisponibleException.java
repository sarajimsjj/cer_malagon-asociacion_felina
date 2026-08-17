package com.cermalagon.backend.exception;

public class GatoNoDisponibleException extends RuntimeException {
    public GatoNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
