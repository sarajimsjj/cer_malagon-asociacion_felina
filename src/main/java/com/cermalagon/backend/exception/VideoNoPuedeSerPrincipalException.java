package com.cermalagon.backend.exception;

public class VideoNoPuedeSerPrincipalException extends RuntimeException {
    public VideoNoPuedeSerPrincipalException() {
        super("Un vídeo no puede marcarse como foto principal");
    }
}
