package com.cermalagon.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

/** Sirve las fotos guardadas en disco (ver AlmacenadorArchivos) bajo /uploads/**. */
@Configuration
public class UploadsConfig implements WebMvcConfigurer {

    private final Path directorioSubidas;

    public UploadsConfig(@Value("${app.uploads.dir}") String directorioConfigurado) {
        this.directorioSubidas = Path.of(directorioConfigurado).toAbsolutePath().normalize();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + directorioSubidas + "/");
    }
}
