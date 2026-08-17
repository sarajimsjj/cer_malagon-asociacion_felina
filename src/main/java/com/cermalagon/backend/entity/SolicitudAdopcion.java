package com.cermalagon.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitudes_adopcion")
@Getter
@Setter
@NoArgsConstructor
public class SolicitudAdopcion {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "gato_id", nullable = false)
    private UUID gatoId;

    @Column(name = "nombre_solicitante", nullable = false, length = 150)
    private String nombreSolicitante;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 30)
    private String telefono;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(nullable = false)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    // Igual que en Gato: las rellena la base de datos (DEFAULT now() + trigger).
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", insertable = false, updatable = false)
    private LocalDateTime fechaActualizacion;
}
