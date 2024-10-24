package com.trello_talk.trello_talk.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.model.Operation;
import com.trello_talk.trello_talk.repository.OperationRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;

    public void saveOperation(String board, String action, boolean outcome, String errorMessage, String ip) {
        try {

            if (errorMessage != null && errorMessage.length() > 255) {
                errorMessage = errorMessage.substring(0, 255);
            }
            operationRepository.save(Operation.builder()
                    .workItem(board)
                    .action(action)
                    .outcome(outcome)
                    .errorMessage(errorMessage)
                    .ip(ip)
                    .timestamp(Calendar.getInstance())
                    .build());
        } catch (Exception e) {
            log.error("Errore durante il salvataggio dell'operazione: " + action);
            throw new ApiException("Errore durante il salvataggio dell'operazione: " + action);
        }
    }

    public <T> Mono<T> handleOperationError(Throwable e, String workItem, String action, String clientIp,
            String errorMessage) {
        saveOperation(workItem, action, Boolean.FALSE, e.getMessage(), clientIp);
        log.error(errorMessage, e);
        return Mono.error(new ApiException(errorMessage, e));
    }

    public <T> Mono<T> handleErrorStatus(String workItem, String action, String clientIp, String errorMessage) {
        saveOperation(workItem, action, Boolean.FALSE, errorMessage, clientIp);
        return Mono.error(new ApiException(errorMessage));
    }

    public void handleErrorStatusNoMono(String workItem, String action, String clientIp, String errorMessage) {
        saveOperation(workItem, action, Boolean.FALSE, errorMessage, clientIp);
        throw new ApiException(errorMessage);
    }
}
