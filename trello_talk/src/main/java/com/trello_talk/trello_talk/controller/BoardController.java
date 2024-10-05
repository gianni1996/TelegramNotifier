package com.trello_talk.trello_talk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.output.BoardOutputDTO;
import com.trello_talk.trello_talk.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/boards")
    public ResponseEntity<List<BoardOutputDTO>> getAllBoards(
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        List<BoardOutputDTO> boards = boardService.getAllBoards(token, apiKey);

        if (boards == null || boards.isEmpty()) {
            log.error("Nessun board trovato.");
            throw new ApiException("No boards found for the user.");
        }

        return ResponseEntity.ok(boards);
    }

}
