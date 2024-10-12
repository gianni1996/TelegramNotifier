package com.trello_talk.trello_talk.service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.input.TrelloListInputDTO;
import com.trello_talk.trello_talk.dto.output.ListWithCardsDTO;
import com.trello_talk.trello_talk.dto.output.TrelloListOutputDTO;
import com.trello_talk.trello_talk.dto.response.CardListResponse;
import com.trello_talk.trello_talk.dto.response.TrelloListListResponse;
import com.trello_talk.trello_talk.dto.response.TrelloListResponse;
import com.trello_talk.trello_talk.mapper.TrelloListMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrelloListService {

    @Autowired
    private TrelloListMapper trelloListMapper;

    @Autowired
    private CardService cardService;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    public List<ListWithCardsDTO> getListsWithCards(String boardId, String token, String apiKey) {
        log.info("Recupero le liste con le loro carte per il boardId: {}", boardId);
        TrelloListListResponse lists = getLists(boardId, token, apiKey);

        return lists.getLists().stream()
                .map(list -> mapListWithCards(list, token, apiKey))
                .collect(Collectors.toList());
    }

    public TrelloListListResponse getLists(String boardId, String token, String apiKey) {
        log.info("Recupero le liste per il boardId: {}", boardId);

        try {
            List<TrelloListInputDTO> lists = getListsFromBoard(boardId, token, apiKey);

            if (lists == null || lists.isEmpty()) {
                log.error("Nessuna lista trovata per il boardId: " + boardId);
                return new TrelloListListResponse();
            }

            List<TrelloListOutputDTO> trelloListOutputDTOs = lists.stream()
                    .map(trelloListMapper::toOutputDto)
                    .collect(Collectors.toList());

            return new TrelloListListResponse(trelloListOutputDTOs);

        } catch (Exception e) {
            log.error("Errore nel recupero delle liste per il boardId: " + boardId, e);
            throw new ApiException("Impossibile recuperare le liste per il board ID: " + boardId, e);
        }
    }

    public TrelloListResponse createList(String boardId, String name, String token, String apiKey) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
            String url = String.format("https://api.trello.com/1/lists?name=%s&idBoard=%s&key=%s&token=%s", encodedName,
                    boardId,
                    apiKey, token);
            log.info("Invio richiesta POST per creare una nuova lista per il boardId: {}", boardId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            log.info("Risposta ricevuta con stato: {}", response.statusCode());
            log.info("Corpo della risposta: {}", response.body());

            if (response.statusCode() != 200) {
                throw new ApiException("Errore nella creazione della lista: " + response.statusCode());
            }

            TrelloListOutputDTO trelloListOutputDTO = objectMapper.readValue(response.body(),
                    TrelloListOutputDTO.class);

            return new TrelloListResponse(trelloListOutputDTO);

        } catch (Exception e) {
            log.error("Errore durante la creazione della lista", e);
            throw new ApiException("Errore durante la creazione della lista", e);
        }
    }

    public void deleteList(String listId, String apiKey, String token) {
        String url = String.format("https://api.trello.com/1/lists/%s/closed?value=true&key=%s&token=%s", listId,
                apiKey, token);
        log.info("Invio richiesta PUT per eliminare la lista con ID: {}", listId);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            log.info("Risposta ricevuta con stato: {}", response.statusCode());

            if (response.statusCode() != 200) {
                throw new ApiException("Errore nell'eliminazione della lista: " + response.statusCode());
            }

            log.info("Lista con ID: {} eliminata con successo.", listId);

        } catch (Exception e) {
            log.error("Errore durante l'eliminazione della lista con ID: " + listId, e);
            throw new ApiException("Errore durante l'eliminazione della lista con ID: " + listId, e);
        }
    }

    private ListWithCardsDTO mapListWithCards(TrelloListOutputDTO list, String token, String apiKey) {
        log.info("Mappo la lista con le sue carte per la listaId: {}", list.getId());
        CardListResponse cards = cardService.getCards(list.getId(), token, apiKey);
        return new ListWithCardsDTO(list, cards.getCards());
    }

    protected List<TrelloListInputDTO> getListsFromBoard(String boardId, String token, String apiKey) {

        try {
            String url = String.format("https://api.trello.com/1/boards/%s/lists?key=%s&token=%s", boardId, apiKey,
                    token);
            log.info("Invio richiesta GET a: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            log.info("Risposta ricevuta con stato: {}", response.statusCode());
            log.info("Corpo della risposta: {}", response.body());

            if (response.statusCode() != 200) {
                throw new ApiException("Errore nella richiesta: " + response.statusCode());
            }

            return objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, TrelloListInputDTO.class));

        } catch (Exception e) {
            log.error("Errore durante la comunicazione con Trello", e);
            throw new ApiException("Errore durante la comunicazione con Trello", e);
        }
    }
}
