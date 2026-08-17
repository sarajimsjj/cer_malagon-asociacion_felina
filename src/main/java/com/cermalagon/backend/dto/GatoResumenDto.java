package com.cermalagon.backend.dto;
import com.cermalagon.backend.entity.EstadoGato;
import com.cermalagon.backend.entity.Gato;
import com.cermalagon.backend.entity.ResultadoTest;
import com.cermalagon.backend.entity.Sexo;
import com.cermalagon.backend.util.CalculadoraEdad;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Lo que ve el frontend: no exponemos la entidad JPA directamente,
 * y aquí ya convertimos la fecha de nacimiento en el texto de edad.
 * fechaNacimientoEstim se mantiene también en crudo porque el formulario
 * de edición necesita precargar el <input type="date">.
 */
public record GatoResumenDto(
                             UUID id,
                             String nombre,
                             String edadTexto,
                             LocalDate fechaNacimientoEstim,
                             Sexo sexo,
                             boolean esterilizado,
                             boolean desparasitado,
                             boolean vacunado,
                             String enfermedad,
                             EstadoGato estado,
                             ResultadoTest testFiv,
                             ResultadoTest testFelv,
                             String observaciones,
                             String fotoPrincipalUrl
) {
    public static GatoResumenDto desde(Gato gato, String fotoPrincipalUrl) {
        return new GatoResumenDto(
                gato.getId(),
                gato.getNombre(),
                CalculadoraEdad.calcularTexto(gato.getFechaNacimientoEstim()),
                gato.getFechaNacimientoEstim(),
                gato.getSexo(),
                gato.isEsterilizado(),
                gato.isDesparasitado(),
                gato.isVacunado(),
                gato.getEnfermedad(),
                gato.getEstado(),
                gato.getTestFiv(),
                gato.getTestFelv(),
                gato.getObservaciones(),
                fotoPrincipalUrl
        );
    }
}
