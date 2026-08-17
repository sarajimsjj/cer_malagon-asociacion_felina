package com.cermalagon.backend.exception;

public class TipoArchivoNoPermitidoException extends RuntimeException {
    public TipoArchivoNoPermitidoException(String mensaje) {
        super(mensaje);
    }
}
