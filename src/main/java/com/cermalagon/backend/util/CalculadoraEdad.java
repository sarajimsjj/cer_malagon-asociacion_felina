package com.cermalagon.backend.util;


import java.time.LocalDate;
import java.time.Period;

/**
 * Convierte una fecha de nacimiento estimada en un texto legible,
 * ej: "2 meses", "1 año y medio", "3 años".
 */
public class CalculadoraEdad {

    private CalculadoraEdad() {
        // clase de utilidades, no se instancia
    }

    public static String calcularTexto(LocalDate fechaNacimientoEstim) {
        Period periodo = Period.between(fechaNacimientoEstim, LocalDate.now());
        int anios = periodo.getYears();
        int meses = periodo.getMonths();

        if (anios == 0 && meses == 0) {
            return "recién nacido";
        }

        if (anios == 0) {
            return meses == 1 ? "1 mes" : meses + " meses";
        }

        String textoAnios = anios == 1 ? "1 año" : anios + " años";

        if (meses == 0) {
            return textoAnios;
        }
        if (meses == 6) {
            return textoAnios + " y medio";
        }
        String textoMeses = meses == 1 ? "1 mes" : meses + " meses";
        return textoAnios + " y " + textoMeses;
    }
}

