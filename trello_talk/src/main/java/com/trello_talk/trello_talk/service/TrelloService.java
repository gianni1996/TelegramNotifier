package com.trello_talk.trello_talk.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.input.BoardInputDTO;
import com.trello_talk.trello_talk.dto.input.CardInputDTO;
import com.trello_talk.trello_talk.dto.input.ListInputDTO;
import com.trello_talk.trello_talk.dto.output.BoardOutputDTO;
import com.trello_talk.trello_talk.dto.output.CardOutputDTO;
import com.trello_talk.trello_talk.dto.output.ListOutputDTO;
import com.trello_talk.trello_talk.dto.output.ListWithCardsDTO;
import com.trello_talk.trello_talk.mapper.BoardMapper;
import com.trello_talk.trello_talk.mapper.CardMapper;
import com.trello_talk.trello_talk.mapper.ListMapper;
import com.trello_talk.trello_talk.repository.TrelloRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrelloService {

    @Autowired
    private TrelloRepository trelloRepository;

    @Autowired
    private BoardMapper trelloBoardMapper;

    @Autowired
    private ListMapper trelloListMapper;

    @Autowired
    private CardMapper trelloCardMapper;

    @Autowired
    private ObjectMapper objectMapper;

    public List<BoardOutputDTO> getAllBoards(String token, String apiKey) {
        log.info("Recupero i boards. ");
        try {
            List<BoardInputDTO> lists = getBoards(token, apiKey);

            if (lists == null || lists.isEmpty()) {
                throw new ApiException("No boards found for this user. ");
            }

            return lists.stream()
                    .map(trelloBoardMapper::toOutputDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("No boards found for this user. ");
            throw new ApiException("No boards found for this user. ", e);
        }
    }

    public List<ListWithCardsDTO> getListsWithCards(String boardId, String token, String apiKey) {
        log.info("Recupero le liste con le loro carte per il boardId: {}", boardId);
        List<ListOutputDTO> lists = getLists(boardId, token, apiKey);

        return lists.stream()
                .map(list -> mapListWithCards(list, token, apiKey))
                .collect(Collectors.toList());
    }

    public List<ListOutputDTO> getLists(String boardId, String token, String apiKey) {
        log.info("Recupero le liste per il boardId: {}", boardId);
        try {
            List<ListInputDTO> lists = getListsFromBoard(boardId, token, apiKey);

            if (lists == null || lists.isEmpty()) {
                throw new ApiException("No lists found for board ID: " + boardId);
            }

            return lists.stream()
                    .map(trelloListMapper::toOutputDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Errore nel recupero delle liste per il boardId: " + boardId, e);
            throw new ApiException("Unable to retrieve lists for board ID: " + boardId, e);
        }
    }

    public List<CardOutputDTO> getCards(String listId, String token, String apiKey) {
        log.info("Recupero le carte per la listaId: {}", listId);
        try {
            List<CardInputDTO> cards = getCardsFromList(listId, token, apiKey);

            if (cards == null || cards.isEmpty()) {
                return List.of();
            }

            List<CardOutputDTO> outputList = cards.stream()
                    .map(trelloCardMapper::toOutputDto)
                    .collect(Collectors.toList());

            return outputList;
        } catch (Exception e) {
            log.error("Errore nel recupero delle card per la listaId: " + listId, e);
            throw new ApiException("Unable to retrieve cards for list ID: " + listId, e);
        }
    }

    private ListWithCardsDTO mapListWithCards(ListOutputDTO list, String token, String apiKey) {
        log.info("Mappo la lista con le sue carte per la listaId: {}", list.getId());
        List<CardOutputDTO> cards = getCards(list.getId(), token, apiKey);
        return new ListWithCardsDTO(list, cards);
    }

    protected List<BoardInputDTO> getBoards(String token, String apiKey) {
        String url = "https://api.trello.com/1/members/me/boards?key=" + apiKey + "&token=" + token;
        log.info("Invio richiesta GET a: {}", url);
        String jsonResponse = sendGetRequest(url);
        log.info("Risposta ricevuta: {}", jsonResponse);
        return parseJsonToList(jsonResponse, new TypeReference<List<BoardInputDTO>>() {
        });
    }

    protected List<ListInputDTO> getListsFromBoard(String boardId, String token, String apiKey) {
        String url = "https://api.trello.com/1/boards/" + boardId + "/lists?key=" + apiKey + "&token="
                + token;
        log.info("Invio richiesta GET a: {}", url);
        String jsonResponse = sendGetRequest(url);
        log.info("Risposta ricevuta: {}", jsonResponse);
        return parseJsonToList(jsonResponse, new TypeReference<List<ListInputDTO>>() {
        });
    }
    protected List<CardInputDTO> getCardsFromList(String listId, String token, String apiKey) {
        String url = "https://api.trello.com/1/lists/" + listId + "/cards?fields=id,name,desc&key="
                + apiKey + "&token=" + token;
        log.info("Invio richiesta GET a: {}", url);
        String jsonResponse = sendGetRequest(url);
        log.info("Risposta ricevuta: {}", jsonResponse);
        return parseJsonToList(jsonResponse, new TypeReference<List<CardInputDTO>>() {
        });
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