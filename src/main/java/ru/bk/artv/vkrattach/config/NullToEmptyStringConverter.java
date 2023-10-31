package ru.bk.artv.vkrattach.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NullToEmptyStringConverter implements AttributeConverter<String, String>{

    @Override
    public String convertToDatabaseColumn(String s) {
        return s;
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return s == null ? "" : s;
    }
}
