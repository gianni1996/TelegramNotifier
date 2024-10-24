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

import com.trello_talk.trello_talk.dto.request.ListCreateRequest;
import com.trello_talk.trello_talk.dto.request.ListUpdateRequest;
import com.trello_talk.trello_talk.dto.response.TrelloListListResponse;
import com.trello_talk.trello_talk.dto.response.TrelloListResponse;
import com.trello_talk.trello_talk.service.TrelloListService;
import com.trello_talk.trello_talk.util.Constants;
import com.trello_talk.trello_talk.util.IpTracker;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/list")
@Slf4j
public class TrelloListController {

    @Autowired
    private TrelloListService trelloListService;
    @Autowired
    private IpTracker ipTracker;

    @GetMapping("/getall/board/{boardId}")
    public Mono<ResponseEntity<TrelloListListResponse>> getListsByBoardId(
            @PathVariable String boardId,
            @RequestHeader(Constants.HEADER_TOKEN) String token,
            @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
            HttpServletRequest request) {

        String clientIp = ipTracker.getClientIp(request);
        log.info(Constants.LIST_GET_REQUEST_MESSAGE, boardId, clientIp);
        return trelloListService.getListsWithCards(boardId, token, apiKey, clientIp).map(ResponseEntity::ok);
    }

    @PostMapping("/create/board/{boardId}")
    public Mono<ResponseEntity<TrelloListResponse>> createList(
            @PathVariable String boardId,
            @RequestBody ListCreateRequest createListRequestDTO,
            @RequestHeader(Constants.HEADER_TOKEN) String token,
            @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
            HttpServletRequest request) {

        String clientIp = ipTracker.getClientIp(request);
        String name = createListRequestDTO.getName();
        log.info(Constants.CARD_CREATE_REQUEST_MESSAGE, name, clientIp);
        return trelloListService.createList(boardId, name, token, apiKey, clientIp).map(ResponseEntity::ok);
    }

    @DeleteMapping("/delete/{listId}")
    public Mono<ResponseEntity<Void>> deleteList(
            @PathVariable(Constants.LIST_ID) String listId,
            @RequestHeader(Constants.HEADER_TOKEN) String token,
            @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
            HttpServletRequest request) {

        String clientIp = ipTracker.getClientIp(request);
        log.info(Constants.CARD_DELETE_REQUEST_MESSAGE, listId, clientIp);
    
        return trelloListService.deleteList(listId, apiKey, token, clientIp).map(v -> ResponseEntity.noContent().build());
    }

    @PutMapping("/update/{listId}")
    public Mono<ResponseEntity<TrelloListResponse>> updateList(
            @PathVariable(Constants.LIST_ID) String listId,
            @RequestBody ListUpdateRequest updateListRequest,
            @RequestHeader(Constants.HEADER_TOKEN) String token,
            @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
            HttpServletRequest request) {

        String name = updateListRequest.getName();
        String clientIp = ipTracker.getClientIp(request);
        log.info(Constants.LIST_UPDATE_REQUEST_MESSAGE, listId, clientIp);

        return trelloListService.updateList(listId, name, token, apiKey, clientIp)
                .map(ResponseEntity::ok);
    }
}
