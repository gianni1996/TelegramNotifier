package com.telegram_notifier.telegram_notifier.repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegram_notifier.telegram_notifier.config.TrelloConfig;
import com.telegram_notifier.telegram_notifier.dto.input.TrelloCardInputDTO;
import com.telegram_notifier.telegram_notifier.dto.input.TrelloListInputDTO;

@Repository
public class TrelloRepository {

    @Autowired
    private TrelloConfig trelloConfig;

    @Autowired
    private ObjectMapper objectMapper; // Usa ObjectMapper per deserializzare JSON

    // Questo metodo restituisce direttamente una lista di TrelloList invece di una
    // stringa
    public List<TrelloListInputDTO> getListsFromBoard(String boardId) throws Exception {
        String url = "https://api.trello.com/1/boards/" + boardId + "/lists?key=" + trelloConfig.getApiKey() + "&token="
                + trelloConfig.getOauthToken();
        String jsonResponse = sendGetRequest(url);

        // Converti la risposta JSON direttamente in una lista di TrelloList
        return objectMapper.readValue(jsonResponse, new TypeReference<List<TrelloListInputDTO>>() {
        });
    }

    // Questo metodo restituisce direttamente una lista di TrelloCard invece di una
    // stringa
    public List<TrelloCardInputDTO> getCardsFromList(String listId) throws Exception {
        String url = "https://api.trello.com/1/lists/" + listId + "/cards?fields=id,name,desc&key="
                + trelloConfig.getApiKey() + "&token=" + trelloConfig.getOauthToken();
        String jsonResponse = sendGetRequest(url);

        // Converti la risposta JSON direttamente in una lista di TrelloCard
        return objectMapper.readValue(jsonResponse, new TypeReference<List<TrelloCardInputDTO>>() {
        });
    }

    // Metodo che invia una richiesta GET e restituisce la risposta JSON
    private String sendGetRequest(String urlString) throws Exception {
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
            throw new Exception("Errore nella richiesta: " + connection.getResponseCode());
        }
    }
}
