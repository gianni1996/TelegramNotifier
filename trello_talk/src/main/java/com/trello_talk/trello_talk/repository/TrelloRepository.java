package com.trello_talk.trello_talk.repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trello_talk.trello_talk.config.TrelloConfig;
import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.input.TrelloCardInputDTO;
import com.trello_talk.trello_talk.dto.input.TrelloListInputDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class TrelloRepository {



    @Autowired
    private TrelloConfig trelloConfig;

    @Autowired
    private ObjectMapper objectMapper;

    public List<TrelloListInputDTO> getListsFromBoard(String boardId) {
        String url = "https://api.trello.com/1/boards/" + boardId + "/lists?key=" + trelloConfig.getApiKey() + "&token=" + trelloConfig.getOauthToken();
        log.info("Invio richiesta GET a: {}", url);
        String jsonResponse = sendGetRequest(url);
        log.info("Risposta ricevuta: {}", jsonResponse);
        return parseJsonToList(jsonResponse, new TypeReference<List<TrelloListInputDTO>>() {});
    }

    public List<TrelloCardInputDTO> getCardsFromList(String listId) {
        String url = "https://api.trello.com/1/lists/" + listId + "/cards?fields=id,name,desc&key=" + trelloConfig.getApiKey() + "&token=" + trelloConfig.getOauthToken();
        log.info("Invio richiesta GET a: {}", url);
        String jsonResponse = sendGetRequest(url);
        log.info("Risposta ricevuta: {}", jsonResponse);
        return parseJsonToList(jsonResponse, new TypeReference<List<TrelloCardInputDTO>>() {});
    }

    private String sendGetRequest(String urlString) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                log.info("Errore nella richiesta, codice di stato: {}", connection.getResponseCode());
                throw new ApiException("Errore nella richiesta: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            log.info("Errore durante l'invio della richiesta: {}", e.getMessage(), e);
            throw new ApiException("Si è verificato un errore durante l'invio della richiesta: " + e.getMessage(), e);
        }
    }

    private <T> List<T> parseJsonToList(String jsonResponse, TypeReference<List<T>> typeReference) {
        try {
            return objectMapper.readValue(jsonResponse, typeReference);
        } catch (Exception e) {
            log.info("Errore nella deserializzazione della risposta JSON: {}", e.getMessage(), e);
            throw new ApiException("Errore nella deserializzazione della risposta JSON: " + e.getMessage(), e);
        }
    }
}

