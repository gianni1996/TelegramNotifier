package com.trello_talk.trello_talk.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.input.CardInputDTO;
import com.trello_talk.trello_talk.dto.output.CardOutputDTO;
import com.trello_talk.trello_talk.mapper.CardMapper;
import com.trello_talk.trello_talk.util.HttpRequestUtil;
import com.trello_talk.trello_talk.util.JsonParserUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CardService {
   
    @Autowired
    private CardMapper trelloCardMapper;

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

    protected List<CardInputDTO> getCardsFromList(String listId, String token, String apiKey) {
        String url = "https://api.trello.com/1/lists/" + listId + "/cards?fields=id,name,desc&key="
                + apiKey + "&token=" + token;
        log.info("Invio richiesta GET a: {}", url);
        String jsonResponse = HttpRequestUtil.sendGetRequest(url);
        log.info("Risposta ricevuta: {}", jsonResponse);
        return JsonParserUtil.parseJsonToList(jsonResponse, new TypeReference<List<CardInputDTO>>() {
        });
    }
}
