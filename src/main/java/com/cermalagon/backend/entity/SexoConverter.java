package com.cermalagon.backend.entity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convierte Sexo.MACHO <-> "macho" para que coincida con el CHECK
 * de la columna "sexo" en la base de datos (valores en minúsculas).
 */
@Converter(autoApply = true)
public class SexoConverter implements AttributeConverter<Sexo, String> {

    @Override
    public String convertToDatabaseColumn(Sexo sexo) {
        return sexo == null ? null : sexo.name().toLowerCase();
    }

    @Override
    public Sexo convertToEntityAttribute(String dbValue) {
        return dbValue == null ? null : Sexo.valueOf(dbValue.toUpperCase());
    }
}