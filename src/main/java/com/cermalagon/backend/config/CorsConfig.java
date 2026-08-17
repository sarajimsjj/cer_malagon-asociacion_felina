package com.cermalagon.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración CORS centralizada (sustituye a @CrossOrigin en los controladores).
 * En local se permite cualquier puerto de localhost porque Vite no siempre usa el 5173
 * (si está ocupado, prueba automáticamente con 5174, 5175...). En producción hay que
 * añadir el dominio real del frontend (ej. https://cer-malagon.vercel.app) a través de
 * la variable de entorno CORS_ALLOWED_ORIGINS (varios orígenes separados por comas).
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String[] origenesPermitidos;

    public CorsConfig(@Value("${app.cors.allowed-origins:http://localhost:*}") String origenesPermitidos) {
        this.origenesPermitidos = origenesPermitidos.split(",");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns(origenesPermitidos)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}
