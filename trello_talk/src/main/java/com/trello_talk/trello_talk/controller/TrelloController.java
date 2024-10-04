package com.trello_talk.trello_talk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.output.BoardOutputDTO;
import com.trello_talk.trello_talk.dto.output.CardOutputDTO;
import com.trello_talk.trello_talk.dto.output.ListOutputDTO;
import com.trello_talk.trello_talk.dto.output.ListWithCardsDTO;
import com.trello_talk.trello_talk.service.TrelloService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TrelloController {

    @Autowired
    private TrelloService trelloService;

    @GetMapping("/boards")
    public ResponseEntity<List<BoardOutputDTO>> getAllBoards(
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        List<BoardOutputDTO> boards = trelloService.getAllBoards(token, apiKey);

        if (boards == null || boards.isEmpty()) {
            log.error("Nessun board trovato.");
            throw new ApiException("No boards found for the user.");
        }

        return ResponseEntity.ok(boards);
    }

    @GetMapping("/board/{boardId}/lists")
    public ResponseEntity<List<ListOutputDTO>> getListsByBoardId(
            @PathVariable String boardId,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        List<ListOutputDTO> lists = trelloService.getLists(boardId, token, apiKey);

        if (lists == null || lists.isEmpty()) {
            log.error("Nessuna lista trovata per il boardId: " + boardId);
            throw new ApiException("No lists found for board ID: " + boardId);
        }

        return ResponseEntity.ok(lists);
    }

    @GetMapping("/board/{listId}/cards")
    public ResponseEntity<List<CardOutputDTO>> getCardsByListId(
            @PathVariable String listId,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        List<CardOutputDTO> cards = trelloService.getCards(listId, token, apiKey);

        if (cards == null || cards.isEmpty()) {
            log.error("Nessuna card trovata per la listId: " + listId);
            throw new ApiException("No cards found for list ID: " + listId);
        }

        return ResponseEntity.ok(cards);
    }

    @GetMapping("/board/{boardId}/listswithcards")
    public ResponseEntity<List<ListWithCardsDTO>> getListsWithCardsByBoardId(
            @PathVariable String boardId,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        List<ListWithCardsDTO> listWithCardsDTOs = trelloService.getListsWithCards(boardId, token, apiKey);

        if (listWithCardsDTOs == null || listWithCardsDTOs.isEmpty()) {
            log.error("Nessuna lista trovata per il boardId: " + boardId);
            throw new ApiException("No lists found for board ID: " + boardId);
        }

        return ResponseEntity.ok(listWithCardsDTOs);
    }
}
