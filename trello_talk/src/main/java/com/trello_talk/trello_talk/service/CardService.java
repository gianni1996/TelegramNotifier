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
import com.trello_talk.trello_talk.dto.input.CardInputDTO;
import com.trello_talk.trello_talk.dto.output.CardOutputDTO;
import com.trello_talk.trello_talk.dto.response.CardListResponse;
import com.trello_talk.trello_talk.dto.response.CardResponse;
import com.trello_talk.trello_talk.mapper.CardMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CardService {

    @Autowired
    private CardMapper trelloCardMapper;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    public CardListResponse getCards(String listId, String token, String apiKey) {
        log.info("Recupero le carte per la listaId: {}", listId);
        try {
            List<CardInputDTO> cards = getCardsFromList(listId, token, apiKey);

            if (cards == null || cards.isEmpty()) {
                return new CardListResponse(List.of());
            }

            List<CardOutputDTO> cardOutputDTOs = cards.stream()
                    .map(trelloCardMapper::toOutputDto)
                    .collect(Collectors.toList());

            return new CardListResponse(cardOutputDTOs);

        } catch (Exception e) {
            log.error("Errore nel recupero delle card per la listaId: " + listId, e);
            throw new ApiException("Impossibile recuperare le carte per la lista ID: " + listId, e);
        }
    }

    protected List<CardInputDTO> getCardsFromList(String listId, String token, String apiKey) {
        try {
            String url = String.format("https://api.trello.com/1/lists/%s/cards?fields=id,name,desc&key=%s&token=%s",
                    listId, apiKey, token);
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
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CardInputDTO.class));

        } catch (Exception e) {
            log.error("Errore durante la comunicazione con Trello", e);
            throw new ApiException("Errore durante la comunicazione con Trello", e);
        }
    }

    public CardResponse createCard(String listId, String name, String desc, String token, String apiKey) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
            String encodedDesc = URLEncoder.encode(desc, StandardCharsets.UTF_8.toString());

            String url = String.format("https://api.trello.com/1/cards?idList=%s&name=%s&desc=%s&key=%s&token=%s",
                    listId, encodedName, encodedDesc, apiKey, token);

            log.info("Invio richiesta POST per creare una carta con nome: {} e descrizione: {}", name, desc);

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
                throw new ApiException("Errore durante la creazione della carta: " + response.statusCode());
            }

            CardOutputDTO cardOutputDTO = objectMapper.readValue(response.body(), CardOutputDTO.class);
            return new CardResponse(cardOutputDTO);
        } catch (Exception e) {
            log.error("Errore durante la creazione della carta", e);
            throw new ApiException("Errore durante la creazione della carta", e);
        }
    }

    public void deleteCard(String cardId, String apiKey, String token) {
        try {
            String url = String.format("https://api.trello.com/1/cards/%s?key=%s&token=%s", cardId, apiKey, token);
            log.info("Invio richiesta DELETE per eliminare la carta con ID: {}", cardId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            log.info("Risposta ricevuta con stato: {}", response.statusCode());

            if (response.statusCode() != 200) {
                throw new ApiException("Errore nella richiesta DELETE per la carta: " + response.statusCode());
            }

            log.info("Carta con ID: {} eliminata con successo.", cardId);

        } catch (Exception e) {
            log.error("Errore durante l'eliminazione della carta con ID: " + cardId, e);
            throw new ApiException("Errore durante l'eliminazione della carta con ID: " + cardId, e);
        }
    }
}
