package com.trello_talk.trello_talk.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.model.Operation;
import com.trello_talk.trello_talk.repository.OperationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;

    public void saveOperation(String board, String create, boolean outcome, String errorMessage, String ip) {
        try {
            operationRepository.save(Operation.builder()
                    .workItem(board)
                    .action(create)
                    .outcome(outcome)
                    .errorMessage(errorMessage)
                    .ip(ip)
                    .timestamp(Calendar.getInstance())
                    .build());
        } catch (Exception e) {
            log.error("Errore durante il salvataggio dell'operazione: " + create);
            throw new ApiException("Errore durante il salvataggio dell'operazione: " + create);
        }

    }
}
