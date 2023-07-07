package org.net.users.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(final Map<String, Object> userDetails) {
        String userDetailsJson = null;
        try {
            userDetailsJson = objectMapper.writeValueAsString(userDetails);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }
        return userDetailsJson;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(final String userDetailsJson) {

        Map<String, Object> userDetails = null;
        try {
            userDetails = objectMapper.readValue(userDetailsJson, new TypeReference<HashMap<String, Object>>() {});
        } catch (final IOException e) {
            log.error("JSON reading error", e);
        }
        return userDetails;
    }
}
