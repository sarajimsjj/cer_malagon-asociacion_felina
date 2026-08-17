package com.cermalagon.backend.storage;

import com.cermalagon.backend.entity.TipoMedia;
import com.cermalagon.backend.exception.TipoArchivoNoPermitidoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Guarda las fotos y vídeos de los gatos en un bucket de Amazon S3. Las credenciales
 * (AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY) las coge el SDK directamente de las
 * variables de entorno estándar de AWS; esta clase nunca las lee ni las ve.
 *
 * Si en el futuro se quiere migrar a otro proveedor, basta con sustituir esta clase:
 * el resto de la aplicación solo conoce la URL guardada en BD, no de dónde viene.
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

    private final S3Client s3Client;
    private final String bucket;
    private final String urlBase;

    public AlmacenadorArchivos(
            @Value("${app.aws.s3.bucket}") String bucket,
            @Value("${app.aws.s3.region}") String region
    ) {
        this.bucket = bucket;
        this.s3Client = S3Client.builder().region(Region.of(region)).build();
        this.urlBase = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
    }

    public ArchivoGuardado guardar(MultipartFile archivo) {
        TipoPermitido tipoPermitido = TIPOS_PERMITIDOS.get(archivo.getContentType());
        if (tipoPermitido == null) {
            throw new TipoArchivoNoPermitidoException(
                    "Solo se admiten imágenes JPEG, PNG, WEBP o vídeos MP4, WEBM");
        }

        String nombreArchivo = UUID.randomUUID() + tipoPermitido.extension();

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(nombreArchivo)
                            .contentType(archivo.getContentType())
                            .build(),
                    RequestBody.fromInputStream(archivo.getInputStream(), archivo.getSize())
            );
        } catch (IOException e) {
            throw new IllegalStateException("No se ha podido guardar el archivo en S3", e);
        }

        return new ArchivoGuardado(urlBase + nombreArchivo, tipoPermitido.tipo());
    }

    public void eliminar(String url) {
        String nombreArchivo = url.substring(url.lastIndexOf('/') + 1);

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(nombreArchivo)
                    .build());
        } catch (Exception e) {
            // no bloqueamos el borrado del registro en BD por un fallo al borrar en S3
        }
    }
}
