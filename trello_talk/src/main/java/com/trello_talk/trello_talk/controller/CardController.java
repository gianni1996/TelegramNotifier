package com.trello_talk.trello_talk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trello_talk.trello_talk.dto.request.CardCreateRequest;
import com.trello_talk.trello_talk.dto.request.CardUpdateRequest;
import com.trello_talk.trello_talk.dto.response.CardListResponse;
import com.trello_talk.trello_talk.dto.response.CardResponse;
import com.trello_talk.trello_talk.service.CardService;
import com.trello_talk.trello_talk.util.Constants;
import com.trello_talk.trello_talk.util.IpTracker;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/card")
@Slf4j
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private IpTracker ipTracker;

    @GetMapping("/list/{listId}")
    public Mono<ResponseEntity<CardListResponse>> getCardsByListId(
            @PathVariable(Constants.LIST_ID) String listId,
            @RequestHeader(Constants.HEADER_TOKEN) String token,
            @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
            HttpServletRequest request) {

        String clientIp = ipTracker.getClientIp(request);
        log.info(Constants.CARD_CREATE_REQUEST_MESSAGE, listId, clientIp);
        return cardService.getCards(listId, token, apiKey, clientIp)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/create/list/{listId}")
    public Mono<ResponseEntity<CardResponse>> createCard(
            @PathVariable(Constants.LIST_ID) String listId,
            @RequestBody CardCreateRequest createCardRequestDTO,
            @RequestHeader(Constants.HEADER_TOKEN) String token,
            @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
            HttpServletRequest request) {

        String name = createCardRequestDTO.getName();
        String clientIp = ipTracker.getClientIp(request);
        log.info(Constants.CARD_CREATE_REQUEST_MESSAGE, listId, clientIp);
        return cardService.createCard(listId, name, createCardRequestDTO.getDesc(), token, apiKey, clientIp)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/delete/{cardId}")
    public Mono<ResponseEntity<Void>> deleteCard(
            @PathVariable(Constants.CARD_ID) String cardId,
            @RequestHeader(Constants.HEADER_TOKEN) String token,
            @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
            HttpServletRequest request) {

        String clientIp = ipTracker.getClientIp(request);
        log.info(Constants.CARD_DELETE_REQUEST_MESSAGE, cardId, clientIp);
        return cardService.deleteCard(cardId, apiKey, token, clientIp)
                .map(v -> ResponseEntity.noContent().build());
    }

    @PutMapping("/update/{cardId}")
    public Mono<ResponseEntity<CardResponse>> updateCard(
            @PathVariable(Constants.CARD_ID) String cardId,
            @RequestBody CardUpdateRequest updateCardRequest,
            @RequestHeader(Constants.HEADER_TOKEN) String token,
            @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
            HttpServletRequest request) {

        String name = updateCardRequest.getName();
        String desc = updateCardRequest.getDesc();
        String clientIp = ipTracker.getClientIp(request);
        log.info(Constants.CARD_UPDATE_REQUEST_MESSAGE, name, cardId, clientIp);

        return cardService.updateCard(cardId, name, desc, token, apiKey, clientIp)
                .map(ResponseEntity::ok);
    }

}
