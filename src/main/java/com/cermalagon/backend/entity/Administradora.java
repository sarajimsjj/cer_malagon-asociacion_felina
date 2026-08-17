package com.cermalagon.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "administradoras")
@Getter
@Setter
@NoArgsConstructor
public class Administradora {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 100)
    private String nombreUsuario;

    @Column(name = "contrasena_hash", nullable = false)
    private String contrasenaHash;

    // columnDefinition con DEFAULT para que, si la columna ya existe (administradoras
    // creadas antes de introducir roles), Postgres pueda rellenarla sin fallar el ALTER TABLE.
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, columnDefinition = "varchar(20) default 'ESTANDAR'")
    private RolAdministradora rol = RolAdministradora.ESTANDAR;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
