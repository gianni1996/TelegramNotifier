package com.trello_talk.trello_talk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.output.TrelloCardOutputDTO;
import com.trello_talk.trello_talk.dto.output.TrelloListOutputDTO;
import com.trello_talk.trello_talk.dto.output.TrelloListWithCardsDTO;
import com.trello_talk.trello_talk.service.TrelloService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TrelloController {

    @Autowired
    private TrelloService trelloService;

    @GetMapping("/boards/{boardId}/lists")
    public ResponseEntity<List<TrelloListOutputDTO>> getListsByBosrdId(@PathVariable String boardId) {
        List<TrelloListOutputDTO> lists = trelloService.getLists(boardId);

        // Se non ci sono liste per il boardId fornito, lancia un'eccezione
        if (lists == null || lists.isEmpty()) {
            log.error("Nessuna lista trovata per il boardId: " + boardId);
            throw new ApiException("No lists found for board ID: " + boardId);
        }

        // Restituisci le liste con uno stato HTTP 200 OK
        return ResponseEntity.ok(lists);
    }

    @GetMapping("/lists/{listId}/cards")
    public ResponseEntity<List<TrelloCardOutputDTO>> getCardsBylistId(@PathVariable String listId) {
        List<TrelloCardOutputDTO> cards = trelloService.getCards(listId);
        
        if (cards == null || cards.isEmpty()) {
            log.error("Nessuna card trovata per la listaId " + listId);
            throw new ApiException("No cards found for list ID: " + listId);
        }

        return ResponseEntity.ok(cards);
    }

    @GetMapping("/lists/{boardId}/listswithcards")
    public ResponseEntity<List<TrelloListWithCardsDTO>> getListsWithCardsByBoardId(@PathVariable String boardId) {
        
        List<TrelloListWithCardsDTO> listWithCardsDTOs=trelloService.getListsWithCards(boardId);

        // Se non ci sono liste per il boardId fornito, lancia un'eccezione
        if (listWithCardsDTOs == null || listWithCardsDTOs.isEmpty()) {
            log.error("Nessuna lista trovata per il boardId: " + boardId);
            throw new ApiException("No lists found for board ID: " + boardId);
        }

        // Restituisci le liste con uno stato HTTP 200 OK
        return ResponseEntity.ok(listWithCardsDTOs);
    }
}
