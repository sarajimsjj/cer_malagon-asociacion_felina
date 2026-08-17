package com.cermalagon.backend.storage;

import com.cermalagon.backend.entity.TipoMedia;
import com.cermalagon.backend.exception.TipoArchivoNoPermitidoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

/**
 * Guarda las fotos y vídeos de los gatos en disco local (gratis, sin depender de ningún
 * proveedor externo). El nombre del archivo se genera siempre en el servidor (UUID +
 * extensión derivada del content-type real), nunca a partir del nombre que envía el
 * navegador, para no depender de datos de entrada al construir una ruta de archivo.
 *
 * Si en el futuro se quiere migrar a S3 (o cualquier otro proveedor), basta con
 * sustituir esta clase por una que suba a ese proveedor y devuelva su URL: el resto
 * de la aplicación solo conoce la URL guardada en BD, no de dónde viene.
 */
@Component
public class AlmacenadorArchivos {

    private record TipoPermitido(String extension, TipoMedia tipo) {
    }

    private static final Map<String, TipoPermitido> TIPOS_PERMITIDOS = Map.of(
            "image/jpeg", new TipoPermitido(".jpg", TipoMedia.FOTO),
            "image/png", new TipoPermitido(".png", TipoMedia.FOTO),
            "image/webp", new TipoPermitido(".webp", TipoMedia.FOTO),
            "video/mp4", new TipoPermitido(".mp4", TipoMedia.VIDEO),
            "video/webm", new TipoPermitido(".webm", TipoMedia.VIDEO)
    );

    public record ArchivoGuardado(String url, TipoMedia tipo) {
    }

    private final Path directorioBase;

    public AlmacenadorArchivos(@Value("${app.uploads.dir}") String directorioConfigurado) {
        this.directorioBase = Path.of(directorioConfigurado).toAbsolutePath().normalize();
        try {
            Files.createDirectories(directorioBase);
        } catch (IOException e) {
            throw new IllegalStateException("No se ha podido crear el directorio de subidas: " + directorioBase, e);
        }
    }

    public ArchivoGuardado guardar(MultipartFile archivo) {
        TipoPermitido tipoPermitido = TIPOS_PERMITIDOS.get(archivo.getContentType());
        if (tipoPermitido == null) {
            throw new TipoArchivoNoPermitidoException(
                    "Solo se admiten imágenes JPEG, PNG, WEBP o vídeos MP4, WEBM");
        }

        String nombreArchivo = UUID.randomUUID() + tipoPermitido.extension();
        Path destino = directorioBase.resolve(nombreArchivo);

        try {
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("No se ha podido guardar el archivo", e);
        }

        return new ArchivoGuardado("/uploads/" + nombreArchivo, tipoPermitido.tipo());
    }

    public void eliminar(String url) {
        String nombreArchivo = url.substring(url.lastIndexOf('/') + 1);
        Path archivo = directorioBase.resolve(nombreArchivo).normalize();

        if (!archivo.getParent().equals(directorioBase)) {
            return; // nunca borrar fuera del directorio de subidas
        }

        try {
            Files.deleteIfExists(archivo);
        } catch (IOException e) {
            // no bloqueamos el borrado del registro en BD por un fallo al borrar el archivo físico
        }
    }
}
