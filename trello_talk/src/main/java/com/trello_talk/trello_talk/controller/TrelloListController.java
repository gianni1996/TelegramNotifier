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

import com.trello_talk.trello_talk.dto.request.ListCreateRequest;
import com.trello_talk.trello_talk.dto.response.TrelloListListResponse;
import com.trello_talk.trello_talk.dto.response.TrelloListResponse;
import com.trello_talk.trello_talk.service.TrelloListService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/list")
@Slf4j
public class TrelloListController {

    @Autowired
    private TrelloListService trelloListService;

    @GetMapping("/board/{boardId}/lists")
    public ResponseEntity<TrelloListListResponse> getListsByBoardId(
            @PathVariable String boardId,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        TrelloListListResponse lists = trelloListService.getLists(boardId, token, apiKey);
        return ResponseEntity.ok(lists);
    }

    @PostMapping("/board/{boardId}/lists")
    public ResponseEntity<TrelloListResponse> createList(
            @PathVariable String boardId,
            @RequestBody ListCreateRequest createListRequestDTO,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        String name = createListRequestDTO.getName();
        log.info("Richiesta per creare una nuova lista con nome: {}", name);
        TrelloListResponse list = trelloListService.createList(boardId, name, token, apiKey);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/lists/{listId}")
    public ResponseEntity<Void> deleteList(
            @PathVariable("listId") String listId,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        log.info("Richiesta per eliminare la lista con ID: {}", listId);
        trelloListService.deleteList(listId, apiKey, token);
        return ResponseEntity.noContent().build();
    }
}
