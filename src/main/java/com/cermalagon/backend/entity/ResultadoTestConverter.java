package com.cermalagon.backend.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ResultadoTestConverter implements AttributeConverter<ResultadoTest, String> {

    @Override
    public String convertToDatabaseColumn(ResultadoTest resultado) {
        return resultado == null ? null : resultado.name().toLowerCase();
    }

    @Override
    public ResultadoTest convertToEntityAttribute(String dbValue) {
        return dbValue == null ? null : ResultadoTest.valueOf(dbValue.toUpperCase());
    }
}