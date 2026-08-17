package com.cermalagon.backend.exception;

public class NombreUsuarioDuplicadoException extends RuntimeException {
    public NombreUsuarioDuplicadoException(String nombreUsuario) {
        super("Ya existe una administradora con el usuario '" + nombreUsuario + "'");
    }
}
