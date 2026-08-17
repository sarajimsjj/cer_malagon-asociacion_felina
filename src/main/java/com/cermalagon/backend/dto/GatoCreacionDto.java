package com.cermalagon.backend.dto;

import com.cermalagon.backend.entity.EstadoGato;
import com.cermalagon.backend.entity.ResultadoTest;
import com.cermalagon.backend.entity.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * Lo que envía el formulario de "añadir gato". Separado del DTO de
 * respuesta (GatoResumenDto) porque aquí no tiene sentido pedir edadTexto
 * (se calcula) ni el id (lo genera la base de datos).
 */
public record GatoCreacionDto(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotNull(message = "La fecha de nacimiento estimada es obligatoria")
        @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
        LocalDate fechaNacimientoEstim,

        @NotNull(message = "El sexo es obligatorio")
        Sexo sexo,

        boolean esterilizado,
        boolean desparasitado,
        boolean vacunado,

        String enfermedad,

        @NotNull(message = "El estado es obligatorio")
        EstadoGato estado,

        @NotNull(message = "El resultado del test FIV es obligatorio")
        ResultadoTest testFiv,

        @NotNull(message = "El resultado del test FeLV es obligatorio")
        ResultadoTest testFelv,

        String observaciones
) {
}