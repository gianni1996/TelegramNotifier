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

import com.trello_talk.trello_talk.dto.request.BoardCreateRequest;
import com.trello_talk.trello_talk.dto.response.BoardListResponse;
import com.trello_talk.trello_talk.dto.response.BoardResponse;
import com.trello_talk.trello_talk.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/board")
@Slf4j
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/getall")
    public ResponseEntity<BoardListResponse> getAllBoards(
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        BoardListResponse boards = boardService.getAllBoards(token, apiKey);
        return ResponseEntity.ok(boards);
    }

    @PostMapping("/workspace/{workspaceId}/create")
    public ResponseEntity<BoardResponse> createBoard(
            @PathVariable("workspaceId") String workspaceId,
            @RequestBody BoardCreateRequest createBoardRequestDTO,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        String name = createBoardRequestDTO.getName();
        log.info("Richiesta per creare un nuovo board con nome: {}", name);
        BoardResponse board = boardService.createBoard(name, workspaceId, token, apiKey);

        return ResponseEntity.ok(board);
    }

    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable("boardId") String boardId,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        log.info("Richiesta per eliminare il board con ID: {}", boardId);
        boardService.deleteBoard(boardId, apiKey, token);

        return ResponseEntity.noContent().build();
    }
}