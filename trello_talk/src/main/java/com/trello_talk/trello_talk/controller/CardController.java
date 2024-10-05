package com.trello_talk.trello_talk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.output.CardOutputDTO;
import com.trello_talk.trello_talk.service.CardService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/board/{listId}/cards")
    public ResponseEntity<List<CardOutputDTO>> getCardsByListId(
            @PathVariable String listId,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        List<CardOutputDTO> cards = cardService.getCards(listId, token, apiKey);

        if (cards == null || cards.isEmpty()) {
            log.error("Nessuna card trovata per la listId: " + listId);
            throw new ApiException("No cards found for list ID: " + listId);
        }

        return ResponseEntity.ok(cards);
    }
}
