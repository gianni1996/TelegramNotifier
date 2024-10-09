package com.trello_talk.trello_talk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.request.CardCreateRequest;
import com.trello_talk.trello_talk.dto.response.CardListResponse;
import com.trello_talk.trello_talk.dto.response.CardResponse;
import com.trello_talk.trello_talk.service.CardService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/card")
@Slf4j
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/list/{listId}")
    public ResponseEntity<CardListResponse> getCardsByListId(
            @PathVariable String listId,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        CardListResponse cards = cardService.getCards(listId, token, apiKey);

        if (cards == null || cards.getCards().isEmpty()) {
            log.error("Nessuna card trovata per la listId: " + listId);
            throw new ApiException("No cards found for list ID: " + listId);
        }

        return ResponseEntity.ok(cards);
    }

    @PostMapping("/list/{listId}/create")
    public ResponseEntity<CardResponse> createCard(
            @PathVariable("listId") String listId,
            @RequestBody CardCreateRequest createCardRequestDTO,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        String name = createCardRequestDTO.getName();
        log.info("Richiesta per creare una nuova card con nome: {}", name);
        CardResponse card = cardService.createCard(listId, name, createCardRequestDTO.getDesc(), token, apiKey);

        return ResponseEntity.ok(card);
    }

    @DeleteMapping("/delete/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable("cardId") String cardId,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        log.info("Richiesta per eliminare la carta con ID: {}", cardId);
        cardService.deleteCard(cardId, apiKey, token);

        return ResponseEntity.noContent().build();
    }
}
