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
import com.trello_talk.trello_talk.config.url.CardApiConfig;
import com.trello_talk.trello_talk.dto.input.CardInputDTO;
import com.trello_talk.trello_talk.dto.output.CardOutputDTO;
import com.trello_talk.trello_talk.dto.response.CardListResponse;
import com.trello_talk.trello_talk.dto.response.CardResponse;
import com.trello_talk.trello_talk.enumeration.Action;
import com.trello_talk.trello_talk.enumeration.WorkItem;
import com.trello_talk.trello_talk.mapper.CardMapper;
import com.trello_talk.trello_talk.model.Card;
import com.trello_talk.trello_talk.model.CardBackup;
import com.trello_talk.trello_talk.repository.CardBackupRepository;
import com.trello_talk.trello_talk.repository.CardRepository;
import com.trello_talk.trello_talk.util.Constants;
import com.trello_talk.trello_talk.util.HttpResponseLogger;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
public class CardService {

    @Autowired
    private CardMapper trelloCardMapper;
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardBackupRepository cardBackupRepository;
    @Autowired
    private OperationService operationService;
    @Autowired
    private CardApiConfig cardApiConfig;
    @Autowired
    private HttpRequestBuilder httpRequestBuilder;

    public Mono<CardListResponse> getCards(String listId, String token, String apiKey, String clientIp) {
        log.info(Constants.CARD_GET_MESSAGE, listId);

        return getCardsFromList(listId, token, apiKey, clientIp)
                .flatMap(cards -> {
                    if (cards == null || cards.isEmpty()) {
                        return Mono.just(new CardListResponse(List.of()));
                    }

                    return Flux.fromIterable(cards)
                            .parallel() // Parallelizza l'elaborazione delle card
                            .runOn(Schedulers.parallel()) // Esegui in thread separati
                            .flatMap(card -> Mono.just(trelloCardMapper.toOutputDto(card))
                                    .timeout(Duration.ofSeconds(5)) // Timeout di 5 secondi per prevenire ritardi
                                    .onErrorResume(TimeoutException.class, e -> {
                                        log.error(Constants.CARD_ERROR_TIMEOUT +
                                                listId);
                                        return Mono.empty(); // Ritorna Mono vuoto in caso di timeout
                                    }))
                            .sequential() // Torna a una sequenza ordinata
                            .collectList() // Colleziona i risultati in una lista
                            .flatMap(cardOutputDTOs -> {
                                if (cardOutputDTOs.isEmpty()) {
                                    log.error(Constants.CARDS_NOT_FOUND_FOR_LIST + listId);
                                }
                                operationService.saveOperation(WorkItem.CARD.getName(), Action.GET.getName(),
                                        Boolean.TRUE, null,
                                        clientIp);
                                return Mono.just(new CardListResponse(cardOutputDTOs));
                            });
                })
                .onErrorResume(e -> operationService.handleOperationError(e, WorkItem.CARD.getName(),
                        Action.GET.getName(), clientIp, Constants.CARD_GET_ERROR_MESSAGE));
    }

    protected Mono<List<CardInputDTO>> getCardsFromList(String listId, String token, String apiKey, String clientIp) {
        try {
            String url = String.format(cardApiConfig.getGetCardsUrl(), listId, apiKey, token);
            log.info(Constants.GET_URL_MESSAGE + url);

            HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildGetRequest(url),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);
            log.info(Constants.RESPONSE_RECEIVED_STATUS + statusCode);

            if (statusCode != 200) {
                return operationService.handleErrorStatus(WorkItem.CARD.getName(), Action.GET.getName(), clientIp,
                        Constants.ERROR_REQUEST_MESSAGE + statusCode);
            }

            List<CardInputDTO> cardInputDTOs = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CardInputDTO.class));

            return Mono.just(cardInputDTOs);

        } catch (Exception e) {
            return operationService.handleOperationError(e, WorkItem.CARD.getName(), Action.CREATE.getName(), clientIp,
                    Constants.CARD_GET_ERROR_MESSAGE);
        }
    }

    public Mono<CardResponse> createCard(String listId, String name, String desc, String token, String apiKey,
            String clientIp) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
            String encodedDesc = URLEncoder.encode(desc, StandardCharsets.UTF_8.toString());
            String url = String.format(cardApiConfig.getCreateCardUrl(), listId, encodedName, encodedDesc, apiKey,
                    token);
            log.info(Constants.CARD_CREATE_MESSAGE, name, desc);
            HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildPostRequest(url),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);

            if (statusCode != 200) {
                return operationService.handleErrorStatus(WorkItem.CARD.getName(), Action.CREATE.getName(), clientIp,
                        Constants.ERROR_REQUEST_MESSAGE + statusCode);
            }

            CardOutputDTO cardOutputDTO = objectMapper.readValue(response.body(), CardOutputDTO.class);
            cardRepository.save(trelloCardMapper.toModel(cardOutputDTO));
            operationService.saveOperation(WorkItem.CARD.getName(), Action.GET.getName(), Boolean.TRUE, null, clientIp);
            return Mono.just(new CardResponse(cardOutputDTO));

        } catch (Exception e) {
            return operationService.handleOperationError(e, WorkItem.CARD.getName(), Action.CREATE.getName(), clientIp,
                    Constants.CARD_CREATE_ERROR_MESSAGE);
        }
    }

    public Mono<Void> deleteCard(String cardId, String apiKey, String token, String clientIp) {
        try {
            String url = String.format(cardApiConfig.getDeleteCardUrl(), cardId, apiKey, token);
            log.info(Constants.CARD_DELETE_MESSAGE, cardId);
            HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildDeleteRequest(url),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);
            backupBoardDeletedById(cardId, statusCode, clientIp);
            return Mono.empty();

        } catch (Exception e) {
            return operationService.handleOperationError(e, WorkItem.CARD.getName(), Action.DELETE.getName(), clientIp,
                    Constants.CARD_DELETE_ERROR_MESSAGE + cardId);
        }
    }

    private void backupBoardDeletedById(String cardId, int statusCode, String clientIp) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);

        if (statusCode != 200) {
            operationService.handleErrorStatusNoMono(WorkItem.CARD.getName(), Action.DELETE.getName(), clientIp,
                    Constants.ERROR_REQUEST_MESSAGE + statusCode);
        }

        if (cardOptional.isPresent()) {
            Card cardForBackup = cardOptional.get();
            cardBackupRepository.save(trelloCardMapper.toBackup(cardForBackup));
            operationService.saveOperation(WorkItem.CARD.getName(), Action.DELETE.getName(), Boolean.TRUE, null,
                    clientIp);
            cardRepository.deleteById(cardId);
            log.info(Constants.CARD_DELETED_SUCCESSFULLY, cardId);
        } else {
            operationService.saveOperation(WorkItem.CARD.getName(), Action.DELETE.getName(), Boolean.TRUE,
                    Constants.CARD_DELETE_MESSAGE_NO_BACKUP, clientIp);
        }
    }

    public Mono<CardResponse> updateCard(String cardId, String name, String desc, String token, String apiKey,
            String clientIp) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
            String encodedDesc = URLEncoder.encode(desc, StandardCharsets.UTF_8.toString());
            String url = String.format(cardApiConfig.getUpdateCardUrl(), cardId, encodedName, encodedDesc, apiKey,
                    token);

            log.info(Constants.CARD_UPDATE_MESSAGE, name, desc);
            HttpResponse<String> response = httpClient.send(httpRequestBuilder.buildPutRequest(url),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();
            HttpResponseLogger.logResponse(response, statusCode);
            Card card = objectMapper.readValue(response.body(), Card.class);
            backupCardUpdatedById(card, statusCode, clientIp);
            CardOutputDTO cardOutputDTO = trelloCardMapper.toOutputDto(card);
            operationService.saveOperation(WorkItem.CARD.getName(), Action.UPDATE.getName(), Boolean.TRUE, null,
                    clientIp);
            return Mono.just(new CardResponse(cardOutputDTO));

        } catch (Exception e) {
            return operationService.handleOperationError(e, WorkItem.CARD.getName(), Action.UPDATE.getName(), clientIp,
                    Constants.CARD_UPDATE_ERROR_MESSAGE);
        }
    }

    private void backupCardUpdatedById(Card card, int statusCode, String clientIp) {
        String cardId = card.getId();
        Optional<Card> cardOptional = cardRepository.findById(cardId);

        if (statusCode != 200) {
            operationService.handleErrorStatusNoMono(WorkItem.CARD.getName(), Action.UPDATE.getName(), clientIp,
                    Constants.ERROR_REQUEST_MESSAGE + statusCode);
        }

        if (cardOptional.isPresent()) {
            Card cardForBackup = cardOptional.get();
            cardBackupRepository.save(trelloCardMapper.toBackup(cardForBackup));
            cardRepository.save(card);
            operationService.saveOperation(WorkItem.CARD.getName(), Action.UPDATE.getName(), Boolean.TRUE, null,
                    clientIp);
            log.info(Constants.CARD_UPDATED_SUCCESSFULLY, cardId);
        } else {
            cardRepository.save(card);
            operationService.saveOperation(WorkItem.CARD.getName(), Action.UPDATE.getName(), Boolean.TRUE,
                    Constants.CARD_UPDATE_MESSAGE_NO_BACKUP + cardId, clientIp);
        }
    }

    public void backupAllCards(String listId, String clientIp) {
        try {
            log.info(Constants.BACKUP_CARDS_START);
            List<Card> allCards = cardRepository.findByIdList(listId);
            List<CardBackup> cardBackups = new ArrayList<>();

            for (Card card : allCards) {
                CardBackup cardBackup = trelloCardMapper.toBackup(card);
                cardBackups.add(cardBackup);
            }

            cardBackupRepository.saveAll(cardBackups);
            log.info(Constants.BACKUP_CARDS_SUCCESS, cardBackups.size());
        } catch (Exception e) {
            operationService.saveOperation(WorkItem.CARD.getName(), Action.BACKUP.getName(), Boolean.TRUE,
                    Constants.BACKUP_CARDS_ERROR, clientIp);
        }
    }
}
