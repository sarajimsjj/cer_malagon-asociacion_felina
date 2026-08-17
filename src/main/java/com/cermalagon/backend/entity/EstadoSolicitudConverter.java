package com.cermalagon.backend.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoSolicitudConverter implements AttributeConverter<EstadoSolicitud, String> {

    @Override
    public String convertToDatabaseColumn(EstadoSolicitud estado) {
        return estado == null ? null : estado.name().toLowerCase();
    }

    @Override
    public EstadoSolicitud convertToEntityAttribute(String dbValue) {
        return dbValue == null ? null : EstadoSolicitud.valueOf(dbValue.toUpperCase());
    }
}
