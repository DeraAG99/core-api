package com.api.gateway.model.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@WritingConverter
public class ScopesWriteConverter implements Converter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(List<String> source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write scopes JSONB", e);
        }
    }
}