package com.trello_talk.trello_talk.service;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trello_talk.trello_talk.config.http.HttpRequestBuilder;
import com.trello_talk.trello_talk.config.url.BoardApiConfig;
import com.trello_talk.trello_talk.dto.input.BoardInputDTO;
import com.trello_talk.trello_talk.dto.output.BoardOutputDTO;
import com.trello_talk.trello_talk.dto.output.BoardWithListsDTO;
import com.trello_talk.trello_talk.dto.response.BoardListResponse;
import com.trello_talk.trello_talk.dto.response.BoardResponse;
import com.trello_talk.trello_talk.enumeration.Action;
import com.trello_talk.trello_talk.enumeration.WorkItem;
import com.trello_talk.trello_talk.mapper.BoardMapper;
import com.trello_talk.trello_talk.model.Board;
import com.trello_talk.trello_talk.model.BoardBackup;
import com.trello_talk.trello_talk.repository.BoardBackupRepository;
import com.trello_talk.trello_talk.repository.BoardRepository;
import com.trello_talk.trello_talk.util.Constants;
import com.trello_talk.trello_talk.util.HttpResponseLogger;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
    private BoardBackupRepository boardBackupRepository;
    @Autowired
    private OperationService operationService;
    @Autowired
    private BoardApiConfig boardApiConfig;
    @Autowired
    private HttpRequestBuilder httpRequestBuilder;
    @Autowired
    private TrelloListService trelloListService;

    public Mono<BoardListResponse> getAllBoards(String workspaceId, String token, String apiKey, String clientIp) {
        log.info(Constants.BOARD_GET_MESSAGE);

        return getBoards(workspaceId, token, apiKey, clientIp)
                .flatMap(boards -> {
                    if (boards.isEmpty()) {
                        log.info(Constants.BOARD_NOT_FOUND);
                        return Mono.just(new BoardListResponse(List.of()));
                    }

                    return Flux.fromIterable(boards)
                            .parallel() // Parallelizza l'elaborazione delle board
                            .runOn(Schedulers.parallel()) // Esegui in thread separati
                            .flatMap(
                                    board -> trelloListService.getListsWithCards(board.getId(), token, apiKey, clientIp)
                                            .timeout(Duration.ofSeconds(5)) // Timeout di 5 secondi per evitare ritardi
                                            .onErrorResume(TimeoutException.class, e -> {
                                                log.error(Constants.BOARD_ERROR_TIMEOUT +
                                                        board.getId());
                                                return Mono.empty(); // Ritorna Mono vuoto in caso di timeout
                                            })
                                            .map(trelloListListResponse -> {
                                                // Mappa la board e le sue liste in un DTO di risposta
                                                BoardOutputDTO boardOutputDTO = boardMapper.toOutputDto(board);
                                                return new BoardWithListsDTO(boardOutputDTO,
                                                        trelloListListResponse.getLists());
                                            }))
                            .sequential() // Torna a una sequenza ordinata
                            .collectList() // Colleziona i risultati in una lista
                            .map(BoardListResponse::new); // Mappa nella risposta finale
                })
                .onErrorResume(e -> operationService.handleOperationError(e, WorkItem.BOARD.getName(),
                        Action.GET.getName(), clientIp, Constants.BOARD_GET_ERROR_MESSAGE));
    }

    public Mono<List<BoardInputDTO>> getBoards(String workspaceId, String token, String apiKey, String clientIp) {
        try {
            String url = String.format(boardApiConfig.getGetBoardsUrl(), workspaceId, apiKey, token);
            log.info(Constants.GET_URL_MESSAGE + url);

            HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildGetRequest(url),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);

            if (statusCode != 200) {
                return operationService.handleErrorStatus(WorkItem.BOARD.getName(), Action.GET.getName(), clientIp,
                        Constants.ERROR_REQUEST_MESSAGE + statusCode);
            }

            operationService.saveOperation(WorkItem.BOARD.getName(), Action.GET.getName(), Boolean.TRUE, null,
                    clientIp);

            List<BoardInputDTO> boards = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, BoardInputDTO.class));
            return Mono.just(boards);

        } catch (Exception e) {
            return operationService.handleOperationError(e, WorkItem.BOARD.getName(), Action.GET.getName(), clientIp,
                    Constants.BOARD_GET_ERROR_MESSAGE);
        }
    }

    public Mono<BoardResponse> createBoard(String name, String idWorkspace, String token, String apiKey,
            String clientIp) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
            String url = String.format(boardApiConfig.getCreateBoardUrl(), encodedName, idWorkspace, apiKey, token);
            log.info(Constants.BOARD_CREATE_MESSAGE, name, idWorkspace);
            HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildPostRequest(url),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);

            if (statusCode != 200) {
                return operationService.handleErrorStatus(WorkItem.BOARD.getName(), Action.CREATE.getName(), clientIp,
                        Constants.ERROR_REQUEST_MESSAGE + statusCode);
            }

            BoardOutputDTO boardOutputDTO = objectMapper.readValue(response.body(), BoardOutputDTO.class);
            boardRepository.save(boardMapper.toModel(boardOutputDTO));
            
            /** inserisco le liste che mi genera in  automaticotrello per il nuovo board **/
            trelloListService.backupAllListsByIdBoard(boardOutputDTO.getId(), token, apiKey, clientIp); 
            operationService.saveOperation(WorkItem.BOARD.getName(), Action.CREATE.getName(), Boolean.TRUE, null,
                    clientIp);
            return Mono.just(new BoardResponse(boardOutputDTO));

        } catch (Exception e) {
            return operationService.handleOperationError(e, WorkItem.BOARD.getName(), Action.CREATE.getName(), clientIp,
                    Constants.BOARD_CREATE_ERROR_MESSAGE);
        }
    }

    public Mono<Void> deleteBoard(String boardId, String apiKey, String token, String clientIp) {
        try {
            String url = String.format(boardApiConfig.getDeleteBoardUrl(), boardId, apiKey, token);
            log.info(Constants.BOARD_DELETE_MESSAGE, boardId);
            HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildDeleteRequest(url),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);
            backupBoardDeletedById(boardId, statusCode, clientIp);
            return Mono.empty();

        } catch (Exception e) {
            return operationService.handleOperationError(e, WorkItem.BOARD.getName(), Action.DELETE.getName(), clientIp,
                    Constants.BOARD_DELETE_ERROR_MESSAGE + boardId);
        }

    }

    private void backupBoardDeletedById(String boardId, int statusCode, String clientIp) {
        Optional<Board> boardOptional = boardRepository.findById(boardId);

        if (statusCode != 200) {
            operationService.handleErrorStatusNoMono(WorkItem.BOARD.getName(), Action.DELETE.getName(), clientIp,
                    Constants.ERROR_REQUEST_MESSAGE + statusCode);
        }

        if (boardOptional.isPresent()) {
            Board boardForBackup = boardOptional.get();
            boardBackupRepository.save(boardMapper.toBackup(boardForBackup));
            operationService.saveOperation(WorkItem.BOARD.getName(), Action.DELETE.getName(), Boolean.TRUE, null,
                    clientIp);
            trelloListService.backupAllLists(boardId, clientIp);
            boardRepository.deleteById(boardId);
            log.info(Constants.BOARD_DELETED_SUCCESSFULLY, boardId);
        } else {
            operationService.saveOperation(WorkItem.BOARD.getName(), Action.DELETE.getName(), Boolean.TRUE,
                    Constants.BOARD_DELETE_MESSAGE_NO_BACKUP, clientIp);
        }
    }

    public Mono<BoardResponse> updateBoard(String boardId, String name, String token, String apiKey, String clientIp) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
            String url = String.format(boardApiConfig.getUpdateBoardUrl(), boardId, encodedName, apiKey, token);
            log.info(Constants.BOARD_UPDATE_MESSAGE, name, boardId);
            HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildPutRequest(url),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);
            Board board = objectMapper.readValue(response.body(), Board.class);
            backupBoardUpdatedById(board, statusCode, clientIp);
            BoardOutputDTO boardOutputDTO = boardMapper.toOutputDto(board);
            operationService.saveOperation(WorkItem.BOARD.getName(), Action.UPDATE.getName(), Boolean.TRUE, null,
                    clientIp);
            trelloListService.backupAllLists(boardId, clientIp);
            return Mono.just(new BoardResponse(boardOutputDTO));
        } catch (Exception e) {
            return operationService.handleOperationError(e, WorkItem.BOARD.getName(), Action.UPDATE.getName(), clientIp,
                    Constants.BOARD_UPDATE_ERROR_MESSAGE + boardId);
        }
    }

    private void backupBoardUpdatedById(Board board, int statusCode, String clientIp) {
        String boardId = board.getId();
        Optional<Board> boardOptional = boardRepository.findById(boardId);

        if (statusCode != 200) {
            operationService.handleErrorStatusNoMono(WorkItem.BOARD.getName(), Action.UPDATE.getName(), clientIp,
                    Constants.ERROR_REQUEST_MESSAGE + statusCode);
        }

        if (boardOptional.isPresent()) {
            Board boardForBackup = boardOptional.get();
            boardBackupRepository.save(boardMapper.toBackup(boardForBackup));
            boardRepository.save(board);
            operationService.saveOperation(WorkItem.BOARD.getName(), Action.UPDATE.getName(), Boolean.TRUE, null,
                    clientIp);
            log.info(Constants.BOARD_UPDATED_SUCCESSFULLY, boardId);
        } else {
            boardRepository.save(board);
            operationService.saveOperation(WorkItem.BOARD.getName(), Action.UPDATE.getName(), Boolean.TRUE,
                    Constants.BOARD_UPDATE_MESSAGE_NO_BACKUP + boardId, clientIp);
        }
    }

    public void backupAllBoards(String workspaceId, String clientIp) {
        try {
            log.info(Constants.BACKUP_BOARDS_START);

            List<Board> allBoards = boardRepository.findByIdWorkspace(workspaceId);
            List<BoardBackup> boardBackups = new ArrayList<>();

            for (Board board : allBoards) {
                BoardBackup boardBackup = boardMapper.toBackup(board);
                boardBackups.add(boardBackup);
                trelloListService.backupAllLists(board.getId(), clientIp);
            }

            boardBackupRepository.saveAll(boardBackups);
            log.info(Constants.BACKUP_BOARDS_SUCCESS, boardBackups.size());
        } catch (Exception e) {
            operationService.saveOperation(WorkItem.BOARD.getName(), Action.BACKUP.getName(), Boolean.TRUE,
                    Constants.BACKUP_BOARDS_ERROR, clientIp);
        }
    }
}
