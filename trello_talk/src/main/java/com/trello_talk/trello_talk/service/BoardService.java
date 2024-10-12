package com.trello_talk.trello_talk.service;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trello_talk.trello_talk.config.TrelloBoardApiConfig;
import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.input.BoardInputDTO;
import com.trello_talk.trello_talk.dto.output.BoardOutputDTO;
import com.trello_talk.trello_talk.dto.response.BoardListResponse;
import com.trello_talk.trello_talk.dto.response.BoardResponse;
import com.trello_talk.trello_talk.enumeration.Action;
import com.trello_talk.trello_talk.enumeration.WorkItem;
import com.trello_talk.trello_talk.mapper.BoardMapper;
import com.trello_talk.trello_talk.repository.BoardRepository;
import com.trello_talk.trello_talk.util.Constants;
import com.trello_talk.trello_talk.util.HttpResponseLogger;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardService {

    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private OperationService operationService;
    @Autowired
    private TrelloBoardApiConfig boardApiConfig;

    public Mono<BoardListResponse> getAllBoards(String token, String apiKey, String clientIp) {
        log.info(Constants.BOARD_GET_MESSAGE);
        return getBoards(token, apiKey, clientIp)
                .flatMap(boards -> {
                    if (boards.isEmpty()) {
                        log.info(Constants.BOARD_NOT_FOUND);
                        return Mono.just(new BoardListResponse(List.of()));
                    }

                    List<BoardOutputDTO> boardOutputDTOs = boards.stream()
                            .map(boardMapper::toOutputDto)
                            .collect(Collectors.toList());

                    return Mono.just(new BoardListResponse(boardOutputDTOs));
                })
                .onErrorResume(e -> {
                    operationService.saveOperation(WorkItem.BOARD.getName(), Action.GET.getName(), Boolean.FALSE, e.getMessage(), clientIp);
                    log.error(Constants.BOARD_GET_ERROR_MESSAGE, e);
                    return Mono.error(new ApiException(Constants.BOARD_GET_ERROR_MESSAGE, e));
                });
    }

    protected Mono<List<BoardInputDTO>> getBoards(String token, String apiKey, String clientIp) {
        try {
            String url = String.format(boardApiConfig.getGetBoardsUrl(), apiKey, token);
            log.info(Constants.GET_URL_MESSAGE, url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header(Constants.HEADER_ACCEPT, Constants.CONTENT_TYPE_JSON)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);

            if (statusCode != 200) {
                operationService.saveOperation(WorkItem.BOARD.getName(), Action.GET.getName(), Boolean.FALSE, Constants.ERROR_REQUEST_MESSAGE + statusCode, clientIp);
                return Mono.error(new ApiException(Constants.ERROR_REQUEST_MESSAGE + statusCode));
            }

            operationService.saveOperation(WorkItem.BOARD.getName(), Action.GET.getName(), Boolean.TRUE, null, clientIp);

            List<BoardInputDTO> boards = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, BoardInputDTO.class));
            return Mono.just(boards);

        } catch (Exception e) {
            operationService.saveOperation(WorkItem.BOARD.getName(), Action.GET.getName(), Boolean.FALSE, e.getMessage(), clientIp);
            log.error(Constants.BOARD_GET_ERROR_MESSAGE, e);
            return Mono.error(new ApiException(Constants.BOARD_GET_ERROR_MESSAGE, e));
        }
    }

    public Mono<BoardResponse> createBoard(String name, String idWorkspace, String token, String apiKey, String clientIp) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());

            String url = String.format(boardApiConfig.getCreateBoardUrl(), encodedName, idWorkspace, apiKey, token);
            log.info(Constants.BOARD_CREATE_MESSAGE, name, idWorkspace);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .header(Constants.HEADER_ACCEPT, Constants.CONTENT_TYPE_JSON)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);

            if (statusCode != 200) {
                operationService.saveOperation(WorkItem.BOARD.getName(), Action.CREATE.getName(), Boolean.FALSE, Constants.ERROR_REQUEST_MESSAGE + statusCode, clientIp);
                return Mono.error(new ApiException(Constants.ERROR_REQUEST_MESSAGE + statusCode));
            }

            BoardOutputDTO boardOutputDTO = objectMapper.readValue(response.body(), BoardOutputDTO.class);
            boardOutputDTO.setIdWorkspace(idWorkspace);
            boardRepository.save(boardMapper.toModel(boardOutputDTO));

            operationService.saveOperation(WorkItem.BOARD.getName(), Action.CREATE.getName(), Boolean.TRUE, null, clientIp);
            return Mono.just(new BoardResponse(boardOutputDTO));

        } catch (Exception e) {
            operationService.saveOperation(WorkItem.BOARD.getName(), Action.CREATE.getName(), Boolean.FALSE, e.getMessage(), clientIp);
            log.error(Constants.BOARD_CREATE_ERROR_MESSAGE, name, e);
            return Mono.error(new ApiException(Constants.BOARD_CREATE_ERROR_MESSAGE + name, e));
        }
    }

    public Mono<Void> deleteBoard(String boardId, String apiKey, String token, String clientIp) {
        try {
            String url = String.format(boardApiConfig.getDeleteBoardUrl(), boardId, apiKey, token);
            log.info(Constants.BOARD_DELETE_MESSAGE, boardId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .header(Constants.HEADER_ACCEPT, Constants.CONTENT_TYPE_JSON)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);

            if (statusCode != 200) {
                operationService.saveOperation(WorkItem.BOARD.getName(), Action.DELETE.getName(), Boolean.FALSE, Constants.ERROR_REQUEST_MESSAGE + statusCode, clientIp);
                return Mono.error(new ApiException(Constants.ERROR_REQUEST_MESSAGE + statusCode));
            }

            operationService.saveOperation(WorkItem.BOARD.getName(), Action.DELETE.getName(), Boolean.TRUE, null, clientIp);
            log.info(Constants.BOARD_DELETED_SUCCESSFULLY, boardId);
            return Mono.empty();

        } catch (Exception e) {
            operationService.saveOperation(WorkItem.BOARD.getName(), Action.DELETE.getName(), Boolean.FALSE, e.getMessage(), clientIp);
            log.error(Constants.BOARD_DELETE_ERROR_MESSAGE, boardId, e);
            return Mono.error(new ApiException(Constants.BOARD_DELETE_ERROR_MESSAGE + boardId, e));
        }
    }
}
