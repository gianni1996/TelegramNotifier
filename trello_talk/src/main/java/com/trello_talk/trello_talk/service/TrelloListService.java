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
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trello_talk.trello_talk.config.http.HttpRequestBuilder;
import com.trello_talk.trello_talk.config.url.TrelloListApiConfig;
import com.trello_talk.trello_talk.dto.input.TrelloListInputDTO;
import com.trello_talk.trello_talk.dto.output.ListWithCardsDTO;
import com.trello_talk.trello_talk.dto.output.TrelloListOutputDTO;
import com.trello_talk.trello_talk.dto.response.TrelloListListResponse;
import com.trello_talk.trello_talk.dto.response.TrelloListResponse;
import com.trello_talk.trello_talk.enumeration.Action;
import com.trello_talk.trello_talk.enumeration.WorkItem;
import com.trello_talk.trello_talk.mapper.TrelloListMapper;
import com.trello_talk.trello_talk.model.TrelloList;
import com.trello_talk.trello_talk.model.TrelloListBackup;
import com.trello_talk.trello_talk.repository.TrelloListBackupRepository;
import com.trello_talk.trello_talk.repository.TrelloListRepository;
import com.trello_talk.trello_talk.util.Constants;
import com.trello_talk.trello_talk.util.HttpResponseLogger;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
public class TrelloListService {

        @Autowired
        private HttpClient httpClient;
        @Autowired
        private ObjectMapper objectMapper;
        @Autowired
        private TrelloListMapper trelloListMapper;
        @Autowired
        private TrelloListRepository trelloListRepository;
        @Autowired
        private TrelloListBackupRepository trelloListBackupRepository;
        @Autowired
        private CardService cardService;
        @Autowired
        private OperationService operationService;
        @Autowired
        private HttpRequestBuilder httpRequestBuilder;
        @Autowired
        private TrelloListApiConfig listApiConfig;

        public Mono<TrelloListListResponse> getListsWithCards(String boardId, String token, String apiKey,
                        String clientIp) {
                log.info(Constants.LIST_GET_WITH_CARDS_MESSAGE, boardId);

                return getLists(boardId, token, apiKey, clientIp)
                                .flatMapMany(listsResponse -> Flux.fromIterable(listsResponse))
                                .parallel() // Parallelizza l'elaborazione delle liste
                                .runOn(Schedulers.parallel()) // Usa thread separati per l'esecuzione parallela
                                .flatMap(list -> mapListWithCards(list, token, apiKey, clientIp)
                                                .timeout(Duration.ofSeconds(5)) // Timeout per prevenire ritardi
                                                                                // eccessivi
                                                .onErrorResume(TimeoutException.class, e -> {
                                                        log.error(Constants.CARD_ERROR_TIMEOUT +
                                                                        boardId);
                                                        return Mono.empty(); // Ritorna Mono vuoto in caso di timeout
                                                }))
                                .sequential() // Torna a una sequenza ordinata
                                .collectList() // Colleziona tutti i risultati in una lista
                                .map(mappedLists -> new TrelloListListResponse(mappedLists)) // Mappa la lista nella
                                                                                             // risposta finale
                                .onErrorResume(e -> {
                                        log.error(Constants.LIST_GET_ERROR_MESSAGE, e);
                                        return operationService.handleOperationError(e, WorkItem.LIST.getName(),
                                                        Action.GET.getName(), clientIp,
                                                        Constants.LIST_GET_ERROR_MESSAGE);
                                });
        }

        public Mono<List<TrelloListOutputDTO>> getLists(String boardId, String token, String apiKey, String clientIp) {
                log.info(Constants.LIST_GET_MESSAGE, boardId);

                try {
                        String url = String.format(listApiConfig.getGetListsUrl(), boardId, apiKey, token);
                        log.info(Constants.GET_URL_MESSAGE + url);
                        HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildGetRequest(url),
                                        HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

                        int statusCode = response.statusCode();
                        HttpResponseLogger.logResponse(response, statusCode);

                        if (statusCode != 200) {
                                return operationService.handleErrorStatus(WorkItem.BOARD.getName(),
                                                Action.GET.getName(), clientIp,
                                                Constants.ERROR_REQUEST_MESSAGE + statusCode);
                        }

                        List<TrelloListInputDTO> lists = objectMapper.readValue(response.body(),
                                        objectMapper.getTypeFactory().constructCollectionType(List.class,
                                                        TrelloListInputDTO.class));

                        List<TrelloListOutputDTO> trelloListOutputDTOs = lists.stream()
                                        .map(trelloListMapper::toOutputDto)
                                        .collect(Collectors.toList());

                        return Mono.just(trelloListOutputDTOs);

                } catch (Exception e) {
                        return operationService.handleOperationError(e, WorkItem.LIST.getName(), Action.GET.getName(),
                                        clientIp,
                                        Constants.LIST_GET_ERROR_MESSAGE);
                }
        }

        private Mono<ListWithCardsDTO> mapListWithCards(TrelloListOutputDTO list, String token, String apiKey,
                        String clientIp) {
                log.info(Constants.LIST_MAP_CARDS_MESSAGE, list.getId());
                return cardService.getCards(list.getId(), token, apiKey, clientIp)
                                .map(cardsResponse -> new ListWithCardsDTO(list, cardsResponse.getCards()));
        }

        public Mono<TrelloListResponse> createList(String boardId, String name, String token, String apiKey,
                        String clientIp) {
                try {
                        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
                        String url = String.format(listApiConfig.getCreateListUrl(), encodedName, boardId, apiKey,
                                        token);
                        log.info(Constants.LIST_CREATE_REQUEST_MESSAGE, boardId);

                        HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildPostRequest(url),
                                        HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                        int statusCode = response.statusCode();
                        HttpResponseLogger.logResponse(response, statusCode);

                        if (statusCode != 200) {
                                return operationService.handleErrorStatus(WorkItem.LIST.getName(),
                                                Action.CREATE.getName(), clientIp,
                                                Constants.ERROR_REQUEST_MESSAGE + statusCode);
                        }

                        TrelloListOutputDTO trelloListOutputDTO = objectMapper.readValue(response.body(),
                                        TrelloListOutputDTO.class);
                        trelloListRepository.save(trelloListMapper.toModel(trelloListOutputDTO));
                        operationService.saveOperation(WorkItem.LIST.getName(), Action.CREATE.getName(), Boolean.TRUE,
                                        null,
                                        clientIp);

                        return Mono.just(new TrelloListResponse(trelloListOutputDTO));
                } catch (Exception e) {
                        return operationService.handleOperationError(e, WorkItem.LIST.getName(),
                                        Action.CREATE.getName(), clientIp,
                                        Constants.LIST_CREATE_ERROR_MESSAGE);
                }
        }

        public Mono<Void> deleteList(String listId, String apiKey, String token, String clientIp) {
                try {
                        String url = String.format(listApiConfig.getDeleteListUrl(), listId, apiKey, token);
                        log.info(Constants.LIST_DELETE_REQUEST_MESSAGE, listId);

                        HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildPutRequest(url),
                                        HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

                        int statusCode = response.statusCode();
                        HttpResponseLogger.logResponse(response, statusCode);
                        backupListDeletedById(listId, statusCode, clientIp);
                        return Mono.empty();

                } catch (Exception e) {
                        return operationService.handleOperationError(e, WorkItem.LIST.getName(),
                                        Action.DELETE.getName(), clientIp,
                                        Constants.LIST_DELETE_ERROR_MESSAGE + listId);
                }
        }

        private void backupListDeletedById(String listId, int statusCode, String clientIp) {
                Optional<TrelloList> listOptional = trelloListRepository.findById(listId);

                if (statusCode != 200) {
                        operationService.handleErrorStatusNoMono(WorkItem.BOARD.getName(), Action.DELETE.getName(),
                                        clientIp,
                                        Constants.ERROR_REQUEST_MESSAGE + statusCode);
                }

                if (listOptional.isPresent()) {
                        TrelloList boardForBackup = listOptional.get();
                        trelloListBackupRepository.save(trelloListMapper.toBackup(boardForBackup));
                        operationService.saveOperation(WorkItem.LIST.getName(), Action.DELETE.getName(), Boolean.TRUE,
                                        null,
                                        clientIp);
                        cardService.backupAllCards(listId, clientIp);
                        trelloListRepository.deleteById(listId);
                        log.info(Constants.LIST_DELETED_SUCCESSFULLY, listId);
                } else {
                        operationService.saveOperation(WorkItem.LIST.getName(), Action.DELETE.getName(), Boolean.TRUE,
                                        Constants.LIST_DELETE_MESSAGE_NO_BACKUP, clientIp);
                }
        }

        public Mono<Void> backupAllListsByIdBoard(String boardId, String token, String apiKey, String clientIp) {
                try {
                        String url = String.format(listApiConfig.getGetListsUrl(), boardId, apiKey, token);
                        log.info(Constants.GET_URL_MESSAGE + url);
                        HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildGetRequest(url),
                                        HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

                        int statusCode = response.statusCode();
                        HttpResponseLogger.logResponse(response, statusCode);

                        if (statusCode != 200) {
                                return operationService.handleErrorStatus(WorkItem.LIST.getName(),
                                                Action.CREATE.getName(), clientIp,
                                                Constants.ERROR_REQUEST_MESSAGE + statusCode);

                        }
                        List<TrelloList> lists = objectMapper.readValue(response.body(),
                                        objectMapper.getTypeFactory().constructCollectionType(List.class,
                                                        TrelloList.class));
                        String trelloListId;
                        for (TrelloList trelloList : lists) {
                                trelloListId = trelloList.getId();
                                trelloListRepository.save(trelloList);
                                operationService.saveOperation(WorkItem.LIST.getName(), Action.UPDATE.getName(),
                                                Boolean.TRUE,
                                                Constants.LIST_UPDATE_MESSAGE_NO_BACKUP + trelloListId,
                                                clientIp);
                                trelloListId = null;
                        }
                } catch (Exception e) {
                        return operationService.handleOperationError(e, WorkItem.LIST.getName(),
                                        Action.CREATE.getName(), clientIp,
                                        Constants.LIST_CREATE_ERROR_MESSAGE);
                }
                return Mono.empty();
        }

        public Mono<TrelloListResponse> updateList(String listId, String name, String token, String apiKey,
                        String clientIp) {
                try {
                        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
                        String url = String.format(listApiConfig.getUpdateListUrl(), listId, encodedName, apiKey,
                                        token);
                        log.info(Constants.LIST_UPDATE_REQUEST_MESSAGE, listId, clientIp);

                        HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildPutRequest(url),
                                        HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

                        int statusCode = response.statusCode();
                        HttpResponseLogger.logResponse(response, statusCode);
                        TrelloList trelloList = objectMapper.readValue(response.body(),
                                        TrelloList.class);
                        backupBoardUpdatedById(trelloList, statusCode, clientIp);
                        TrelloListOutputDTO trelloListOutputDTO = trelloListMapper.toOutputDto(trelloList);
                        operationService.saveOperation(WorkItem.LIST.getName(), Action.UPDATE.getName(), Boolean.TRUE,
                                        null,
                                        clientIp);
                        return Mono.just(new TrelloListResponse(trelloListOutputDTO));

                } catch (Exception e) {
                        return operationService.handleOperationError(e, WorkItem.LIST.getName(),
                                        Action.UPDATE.getName(), clientIp,
                                        Constants.LIST_UPDATE_ERROR_MESSAGE);
                }
        }

        private void backupBoardUpdatedById(TrelloList trelloList, int statusCode, String clientIp) {
                String trelloListId = trelloList.getId();
                Optional<TrelloList> trelloListOptional = trelloListRepository.findById(trelloListId);

                if (statusCode != 200) {
                        operationService.handleErrorStatusNoMono(WorkItem.LIST.getName(), Action.UPDATE.getName(),
                                        clientIp,
                                        Constants.ERROR_REQUEST_MESSAGE + statusCode);
                }

                if (trelloListOptional.isPresent()) {
                        TrelloList trelloListBackup = trelloListOptional.get();
                        trelloListBackupRepository.save(trelloListMapper.toBackup(trelloListBackup));
                        trelloListRepository.save(trelloList);
                        operationService.saveOperation(WorkItem.LIST.getName(), Action.UPDATE.getName(), Boolean.TRUE,
                                        null,
                                        clientIp);
                        log.info(Constants.LIST_UPDATED_SUCCESSFULLY, trelloListId);
                } else {
                        trelloListRepository.save(trelloList);
                        operationService.saveOperation(WorkItem.LIST.getName(), Action.UPDATE.getName(), Boolean.TRUE,
                                        Constants.LIST_UPDATE_MESSAGE_NO_BACKUP + trelloListId, clientIp);
                }
        }

        public void backupAllLists(String boardId, String clientIp) {
                try {
                        log.info(Constants.BACKUP_LISTS_START);
                        List<TrelloList> allLists = trelloListRepository.findByIdBoard(boardId);
                        List<TrelloListBackup> listBackups = new ArrayList<>();

                        for (TrelloList list : allLists) {
                                TrelloListBackup listBackup = trelloListMapper.toBackup(list);
                                listBackups.add(listBackup);
                                cardService.backupAllCards(list.getId(), clientIp);
                        }

                        trelloListBackupRepository.saveAll(listBackups);
                        log.info(Constants.BACKUP_LISTS_SUCCESS, listBackups.size());
                } catch (Exception e) {
                        operationService.saveOperation(WorkItem.LIST.getName(), Action.BACKUP.getName(), Boolean.TRUE,
                                        Constants.BACKUP_LISTS_ERROR, clientIp);

                }
        }
}
