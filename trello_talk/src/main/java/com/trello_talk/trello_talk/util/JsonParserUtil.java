package com.trello_talk.trello_talk.util;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trello_talk.trello_talk.config.error.ApiException;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonParserUtil {

    // Static ObjectMapper instance (can be reused for performance optimization)
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Static method to parse JSON to List<T>
    public static <T> List<T> parseJsonToList(String jsonResponse, TypeReference<List<T>> typeReference) {
        try {
            return objectMapper.readValue(jsonResponse, typeReference);
        } catch (Exception e) {
            log.error("Errore nella deserializzazione della risposta JSON: {}", e.getMessage(), e);
            throw new ApiException("Errore nella deserializzazione della risposta JSON: " + e.getMessage(), e);
        }
    }
    
}
