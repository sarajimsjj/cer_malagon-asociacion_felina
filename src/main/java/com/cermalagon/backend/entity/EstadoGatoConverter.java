package com.cermalagon.backend.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convierte EstadoGato.PENDIENTE_ADOPCION_ENFERMEDAD <-> "pendiente_adopcion_enfermedad",
 * para que coincida con el CHECK de la columna "estado" en la base de datos.
 */
@Converter(autoApply = true)
public class EstadoGatoConverter implements AttributeConverter<EstadoGato, String> {

    @Override
    public String convertToDatabaseColumn(EstadoGato estado) {
        return estado == null ? null : estado.name().toLowerCase();
    }

    @Override
    public EstadoGato convertToEntityAttribute(String dbValue) {
        return dbValue == null ? null : EstadoGato.valueOf(dbValue.toUpperCase());
    }
}
