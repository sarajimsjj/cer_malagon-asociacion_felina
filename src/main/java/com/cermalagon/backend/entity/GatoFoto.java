package com.cermalagon.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "gato_fotos")
@Getter
@Setter
@NoArgsConstructor
public class GatoFoto {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "gato_id", nullable = false)
    private UUID gatoId;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(name = "es_principal", nullable = false)
    private boolean esPrincipal = false;

    // columnDefinition con DEFAULT para que, si la columna ya existe (fotos subidas antes
    // de admitir vídeo), Postgres pueda rellenarla sin fallar el ALTER TABLE.
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, columnDefinition = "varchar(10) default 'FOTO'")
    private TipoMedia tipo = TipoMedia.FOTO;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", insertable = false, updatable = false)
    private LocalDateTime fechaActualizacion;
}
