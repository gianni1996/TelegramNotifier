package com.trello_talk.trello_talk.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.input.ListInputDTO;
import com.trello_talk.trello_talk.dto.output.CardOutputDTO;
import com.trello_talk.trello_talk.dto.output.ListOutputDTO;
import com.trello_talk.trello_talk.dto.output.ListWithCardsDTO;
import com.trello_talk.trello_talk.mapper.ListMapper;
import com.trello_talk.trello_talk.util.HttpRequestUtil;
import com.trello_talk.trello_talk.util.JsonParserUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ListService {

    @Autowired
    private ListMapper trelloListMapper;

    @Autowired
    private CardService cardService;

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

    private ListWithCardsDTO mapListWithCards(ListOutputDTO list, String token, String apiKey) {
        log.info("Mappo la lista con le sue carte per la listaId: {}", list.getId());
        List<CardOutputDTO> cards = cardService.getCards(list.getId(), token, apiKey);
        return new ListWithCardsDTO(list, cards);
    }

    protected List<ListInputDTO> getListsFromBoard(String boardId, String token, String apiKey) {
        String url = "https://api.trello.com/1/boards/" + boardId + "/lists?key=" + apiKey + "&token="
                + token;
        log.info("Invio richiesta GET a: {}", url);
        String jsonResponse = HttpRequestUtil.sendGetRequest(url);
        log.info("Risposta ricevuta: {}", jsonResponse);
        return JsonParserUtil.parseJsonToList(jsonResponse, new TypeReference<List<ListInputDTO>>() {
        });
    }

}