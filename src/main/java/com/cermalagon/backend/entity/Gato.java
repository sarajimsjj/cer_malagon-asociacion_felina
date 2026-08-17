package com.cermalagon.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "gatos")
@Getter
@Setter
@NoArgsConstructor
public class Gato {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "fecha_nacimiento_estim", nullable = false)
    private LocalDate fechaNacimientoEstim;

    @Column(nullable = false)
    private Sexo sexo = Sexo.DESCONOCIDO;

    @Column(nullable = false)
    private boolean esterilizado = false;

    @Column(nullable = false)
    private boolean desparasitado = false;

    @Column(nullable = false)
    private boolean vacunado = false;

    @Column(name="test_fiv", nullable = false)
    private ResultadoTest testFiv = ResultadoTest.NO_TESTADO;

    @Column(name="test_felv", nullable = false)
    private ResultadoTest testFelv = ResultadoTest.NO_TESTADO;

    @Column(length = 255)
    private String enfermedad;

    @Column(nullable = false)
    private EstadoGato estado = EstadoGato.DISPONIBLE;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // Estas dos columnas las rellena la base de datos (DEFAULT now() + trigger),
    // por eso son de solo lectura desde el lado de Java.
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", insertable = false, updatable = false)
    private LocalDateTime fechaActualizacion;

}
