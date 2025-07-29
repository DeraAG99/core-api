package com.api.gateway.model.converter;

import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@ReadingConverter
public class ScopesReadConverter implements Converter<Row, List<String>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<String> convert(Row source) {
        try {
            String scopesJson = source.get("scopes", String.class);
            return objectMapper.readValue(scopesJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to read scopes JSONB", e);
        }
    }
}
