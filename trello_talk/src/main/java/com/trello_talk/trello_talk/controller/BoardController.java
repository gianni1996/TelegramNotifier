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

import com.trello_talk.trello_talk.dto.request.BoardCreateRequest;
import com.trello_talk.trello_talk.dto.request.BoardUpdateRequest;
import com.trello_talk.trello_talk.dto.response.BoardListResponse;
import com.trello_talk.trello_talk.dto.response.BoardResponse;
import com.trello_talk.trello_talk.service.BoardService;
import com.trello_talk.trello_talk.util.Constants;
import com.trello_talk.trello_talk.util.IpTracker;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/board")
@Slf4j
public class BoardController {

        @Autowired
        private BoardService boardService;
        @Autowired
        private IpTracker ipTracker;

        @GetMapping("/getall/workspace/{workspaceId}")
        public Mono<ResponseEntity<BoardListResponse>> getAllBoards(
                        @PathVariable String workspaceId,
                        @RequestHeader(Constants.HEADER_TOKEN) String token,
                        @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
                        HttpServletRequest request) {

                String clientIp = ipTracker.getClientIp(request);
                log.info(Constants.BOARD_GET_REQUEST_MESSAGE + clientIp);

                return boardService.getAllBoards(workspaceId, token, apiKey, clientIp)
                                .map(ResponseEntity::ok);
        }

        @PostMapping("/create/workspace/{workspaceId}")
        public Mono<ResponseEntity<BoardResponse>> createBoard(
                        @PathVariable(Constants.WORKSPACE_ID) String workspaceId,
                        @RequestBody BoardCreateRequest createBoardRequestDTO,
                        @RequestHeader(Constants.HEADER_TOKEN) String token,
                        @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
                        HttpServletRequest request) {

                String name = createBoardRequestDTO.getName();
                String clientIp = ipTracker.getClientIp(request);
                log.info(Constants.BOARD_CREATE_REQUEST_MESSAGE, name, clientIp);

                return boardService.createBoard(name, workspaceId, token, apiKey, clientIp)
                                .map(ResponseEntity::ok);
        }

        @DeleteMapping("/delete/{boardId}")
        public Mono<ResponseEntity<Void>> deleteBoard(
                        @PathVariable(Constants.BOARD_ID) String boardId,
                        @RequestHeader(Constants.HEADER_TOKEN) String token,
                        @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
                        HttpServletRequest request) {

                String clientIp = ipTracker.getClientIp(request);
                log.info(Constants.BOARD_DELETE_REQUEST_MESSAGE, boardId, clientIp);

                return boardService.deleteBoard(boardId, apiKey, token, clientIp)
                                .map(v -> ResponseEntity.noContent().build());
        }

        @PutMapping("update/{boardId}")
        public Mono<ResponseEntity<BoardResponse>> updateBoard(
                        @PathVariable(Constants.BOARD_ID) String boardId,
                        @RequestBody BoardUpdateRequest updateBoardRequest,
                        @RequestHeader(Constants.HEADER_TOKEN) String token,
                        @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
                        HttpServletRequest request) {

                String name = updateBoardRequest.getName();
                String clientIp = ipTracker.getClientIp(request);
                log.info(Constants.BOARD_UPDATE_REQUEST_MESSAGE, name, boardId, clientIp);

                return boardService.updateBoard(boardId, name, token, apiKey, clientIp)
                                .map(ResponseEntity::ok);
        }
}