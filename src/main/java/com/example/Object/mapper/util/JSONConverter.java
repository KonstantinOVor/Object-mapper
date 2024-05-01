package com.example.Object.mapper.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.TimeZone;

public class JSONConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true)
                .withTimeZone(TimeZone.getTimeZone("UTC")));
    }

    public static String objectToJSON(Object object) {

        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonToObject(String json, Class<T> valueType) {
        if (json == null) {
            return null;
        }

        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
