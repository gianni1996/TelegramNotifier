package com.trello_talk.trello_talk.service;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trello_talk.trello_talk.config.http.HttpRequestBuilder;
import com.trello_talk.trello_talk.config.url.WorkspaceApiConfig;
import com.trello_talk.trello_talk.dto.input.WorkspaceInputDTO;
import com.trello_talk.trello_talk.dto.output.BoardWithListsDTO;
import com.trello_talk.trello_talk.dto.output.WorkspaceOutputDTO;
import com.trello_talk.trello_talk.dto.output.WorkspaceWithCardsDTO;
import com.trello_talk.trello_talk.dto.response.WorkspaceListResponse;
import com.trello_talk.trello_talk.dto.response.WorkspaceOnlyResponse;
import com.trello_talk.trello_talk.dto.response.WorkspaceResponse;
import com.trello_talk.trello_talk.enumeration.Action;
import com.trello_talk.trello_talk.enumeration.WorkItem;
import com.trello_talk.trello_talk.mapper.WorkspaceMapper;
import com.trello_talk.trello_talk.model.Workspace;
import com.trello_talk.trello_talk.repository.WorkspaceBackupRepository;
import com.trello_talk.trello_talk.repository.WorkspaceRepository;
import com.trello_talk.trello_talk.util.Constants;
import com.trello_talk.trello_talk.util.HttpResponseLogger;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
public class WorkspaceService {

    @Autowired
    private WorkspaceMapper workspaceMapper;
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WorkspaceApiConfig workspaceApiConfig;
    @Autowired
    private OperationService operationService;
    @Autowired
    private HttpRequestBuilder httpRequestBuilder;
    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private WorkspaceBackupRepository workspaceBackupRepository;
    @Autowired
    private BoardService boardService;

    public Mono<WorkspaceOnlyResponse> getOnlyWorkspaces(String token, String apiKey, String clientIp) {
        log.info(Constants.WORKSPACE_GET_MESSAGE);
        return getWorkspaces(token, apiKey, clientIp)
                .flatMap(workspaces -> {
                    if (workspaces.isEmpty()) {
                        log.info(Constants.WORKSPACE_NOT_FOUND);
                        return Mono.just(new WorkspaceOnlyResponse(List.of()));
                    }

                    List<WorkspaceOutputDTO> workspaceOutputDTOs = workspaces.stream()
                            .map(workspaceMapper::toOutputDto)
                            .collect(Collectors.toList());

                    return Mono.just(new WorkspaceOnlyResponse(workspaceOutputDTOs));
                })
                .onErrorResume(e -> operationService.handleOperationError(e, WorkItem.WORKSPACE.getName(),
                        Action.GET.getName(), clientIp, Constants.WORKSPACE_GET_ERROR_MESSAGE));
    }

    public Mono<WorkspaceListResponse> getAllWorkspaces(String token, String apiKey, String clientIp) {
        log.info(Constants.WORKSPACE_GET_MESSAGE);

        return getWorkspaces(token, apiKey, clientIp)
                .flatMap(workspaces -> {
                    if (workspaces.isEmpty()) {
                        log.info(Constants.WORKSPACE_NOT_FOUND);
                        return Mono.just(new WorkspaceListResponse(List.of()));
                    }

                    return Flux.fromIterable(workspaces)
                            .parallel() // Parallelizza le operazioni per ogni workspace
                            .runOn(Schedulers.parallel()) // Usa thread separati per l'esecuzione parallela
                            .flatMap(workspace -> {
                                String workspaceId = workspace.getId();
                                log.error(Constants.WORKSPACE_ERROR_TIMEOUT +
                                        workspaceId);

                                // Chiama il metodo boardService per ottenere le board del workspace
                                return boardService.getAllBoards(workspaceId, token, apiKey, clientIp)
                                        .timeout(Duration.ofSeconds(5)) // Timeout per prevenire ritardi eccessivi
                                        .onErrorResume(TimeoutException.class, e -> {
                                            log.error(Constants.WORKSPACE_ERROR_TIMEOUT +
                                                    workspaceId);
                                            return Mono.empty(); // Se la chiamata fallisce per timeout, ritorna Mono
                                                                 // vuoto
                                        })
                                        .map(boardListResponse -> {
                                            // Mappa workspace e board in un DTO di risposta
                                            WorkspaceOutputDTO workspaceOutputDTO = workspaceMapper
                                                    .toOutputDto(workspace);
                                            List<BoardWithListsDTO> boards = boardListResponse.getBoards();
                                            return new WorkspaceWithCardsDTO(workspaceOutputDTO, boards);
                                        });
                            })
                            .sequential() // Torna a una sequenza ordinata
                            .collectList() // Colleziona tutti i risultati in una lista
                            .map(WorkspaceListResponse::new); // Mappa la lista nella risposta finale
                })
                .onErrorResume(e -> operationService.handleOperationError(e, WorkItem.WORKSPACE.getName(),
                        Action.GET.getName(), clientIp, Constants.WORKSPACE_GET_ERROR_MESSAGE));
    }

    protected Mono<List<WorkspaceInputDTO>> getWorkspaces(String token, String apiKey, String clientIp) {
        try {
            String url = String.format(workspaceApiConfig.getGetWorkspacesUrl(), apiKey, token);
            log.info(Constants.GET_URL_MESSAGE + url);
            HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildGetRequest(url),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);

            if (statusCode != 200) {
                return operationService.handleErrorStatus(WorkItem.WORKSPACE.getName(), Action.GET.getName(), clientIp,
                        Constants.ERROR_REQUEST_MESSAGE + statusCode);
            }

            operationService.saveOperation(WorkItem.WORKSPACE.getName(), Action.GET.getName(), Boolean.TRUE, null,
                    clientIp);

            List<WorkspaceInputDTO> workspaces = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, WorkspaceInputDTO.class));
            return Mono.just(workspaces);

        } catch (Exception e) {
            return operationService.handleOperationError(e, WorkItem.WORKSPACE.getName(), Action.GET.getName(),
                    clientIp,
                    Constants.WORKSPACE_GET_ERROR_MESSAGE);
        }
    }

    public Mono<WorkspaceResponse> createWorkspace(String name, String token, String apiKey, String clientIp) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
            String url = String.format(workspaceApiConfig.getCreateWorkspaceUrl(), encodedName, apiKey, token);
            log.info(Constants.WORKSPACE_CREATE_MESSAGE, name);
            HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildPostRequest(url),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);

            if (statusCode != 200) {
                return operationService.handleErrorStatus(WorkItem.WORKSPACE.getName(), Action.CREATE.getName(),
                        clientIp,
                        Constants.ERROR_REQUEST_MESSAGE + statusCode);
            }

            WorkspaceOutputDTO workspaceOutputDTO = objectMapper.readValue(response.body(), WorkspaceOutputDTO.class);
            workspaceRepository.save(workspaceMapper.toModel(workspaceOutputDTO));
            operationService.saveOperation(WorkItem.WORKSPACE.getName(), Action.CREATE.getName(), Boolean.TRUE, null,
                    clientIp);
            return Mono.just(new WorkspaceResponse(workspaceOutputDTO));

        } catch (Exception e) {
            return operationService.handleOperationError(e, WorkItem.WORKSPACE.getName(), Action.CREATE.getName(),
                    clientIp,
                    Constants.WORKSPACE_CREATE_ERROR_MESSAGE + name);
        }
    }

    public Mono<Void> deleteWorkspace(String workspaceId, String apiKey, String token, String clientIp) {
        try {
            String url = String.format(workspaceApiConfig.getDeleteWorkspaceUrl(), workspaceId, apiKey, token);
            log.info(Constants.WORKSPACE_DELETE_MESSAGE, workspaceId);
            HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildDeleteRequest(url),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);
            backupWorkSpaceDeletedById(workspaceId, statusCode, clientIp);
            operationService.saveOperation(WorkItem.WORKSPACE.getName(), Action.DELETE.getName(), Boolean.TRUE, null,
                    clientIp);
            return Mono.empty();

        } catch (Exception e) {
            return operationService.handleOperationError(e, WorkItem.WORKSPACE.getName(), Action.DELETE.getName(),
                    clientIp,
                    Constants.WORKSPACE_DELETE_ERROR_MESSAGE + workspaceId);
        }
    }

    private void backupWorkSpaceDeletedById(String workspaceId, int statusCode, String clientIp) {
        Optional<Workspace> workspaceOptional = workspaceRepository.findById(workspaceId);

        if (statusCode != 200) {
            operationService.handleErrorStatusNoMono(WorkItem.WORKSPACE.getName(), Action.DELETE.getName(), clientIp,
                    Constants.ERROR_REQUEST_MESSAGE + statusCode);
        }

        if (workspaceOptional.isPresent()) {
            Workspace workspaceForBackup = workspaceOptional.get();
            workspaceBackupRepository.save(workspaceMapper.toBackup(workspaceForBackup));
            operationService.saveOperation(WorkItem.WORKSPACE.getName(), Action.DELETE.getName(), Boolean.TRUE, null,
                    clientIp);
            boardService.backupAllBoards(workspaceId, clientIp);
            workspaceRepository.deleteById(workspaceId);
            log.info(Constants.BOARD_DELETED_SUCCESSFULLY, workspaceId);
        } else {
            operationService.saveOperation(WorkItem.WORKSPACE.getName(), Action.DELETE.getName(), Boolean.TRUE,
                    Constants.BOARD_DELETE_MESSAGE_NO_BACKUP, clientIp);
        }
    }

    public Mono<WorkspaceResponse> updateWorkspace(String workspaceId, String name, String token, String apiKey,
            String clientIp) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
            String url = String.format(workspaceApiConfig.getUpdateWorkspaceUrl(), workspaceId, encodedName, apiKey,
                    token);
            log.info(Constants.WORKSPACE_UPDATE_REQUEST_MESSAGE, workspaceId, clientIp);

            HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildPutRequest(url),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);
            Workspace workspace = objectMapper.readValue(response.body(), Workspace.class);
            backupBoardUpdatedById(workspace, statusCode, clientIp);
            WorkspaceOutputDTO workspaceOutputDTO = workspaceMapper.toOutputDto(workspace);
            operationService.saveOperation(WorkItem.WORKSPACE.getName(), Action.UPDATE.getName(), Boolean.TRUE, null,
                    clientIp);
            return Mono.just(new WorkspaceResponse(workspaceOutputDTO));

        } catch (Exception e) {
            return operationService.handleOperationError(e, WorkItem.WORKSPACE.getName(), Action.UPDATE.getName(),
                    clientIp,
                    Constants.WORKSPACE_UPDATE_ERROR_MESSAGE);
        }
    }

    private void backupBoardUpdatedById(Workspace workspace, int statusCode, String clientIp) {
        String workspaceId = workspace.getId();
        Optional<Workspace> workspaceOptional = workspaceRepository.findById(workspaceId);

        if (statusCode != 200) {
            operationService.handleErrorStatusNoMono(WorkItem.WORKSPACE.getName(), Action.UPDATE.getName(), clientIp,
                    Constants.ERROR_REQUEST_MESSAGE + statusCode);
        }

        if (workspaceOptional.isPresent()) {
            Workspace workspaceForBackup = workspaceOptional.get();
            workspaceBackupRepository.save(workspaceMapper.toBackup(workspaceForBackup));
            workspaceRepository.save(workspace);
            operationService.saveOperation(WorkItem.WORKSPACE.getName(), Action.UPDATE.getName(), Boolean.TRUE, null,
                    clientIp);
            log.info(Constants.WORKSPACE_UPDATED_SUCCESSFULLY, workspaceId);
        } else {
            workspaceRepository.save(workspace);
            operationService.saveOperation(WorkItem.WORKSPACE.getName(), Action.UPDATE.getName(), Boolean.TRUE,
                    Constants.WORKSPACE_UPDATE_MESSAGE_NO_BACKUP + workspaceId, clientIp);
        }
    }
}
