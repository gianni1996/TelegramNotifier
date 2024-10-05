package com.trello_talk.trello_talk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.output.ListOutputDTO;
import com.trello_talk.trello_talk.dto.output.ListWithCardsDTO;
import com.trello_talk.trello_talk.service.ListService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ListController {

    @Autowired
    private ListService trelloService;

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
