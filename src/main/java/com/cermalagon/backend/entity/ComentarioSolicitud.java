package com.cermalagon.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comentarios_solicitud")
@Getter
@Setter
@NoArgsConstructor
public class ComentarioSolicitud {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "solicitud_id", nullable = false)
    private UUID solicitudId;

    // Nombre de usuario de la administradora que escribió el comentario (Administradora.nombreUsuario).
    @Column(nullable = false, length = 100)
    private String autora;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    // A diferencia de SolicitudAdopcion/GatoFoto, aquí la rellena la propia aplicación
    // (como en Administradora): al ser tabla nueva no hace falta el trigger de BD.
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
