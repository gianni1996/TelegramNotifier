package com.trello_talk.trello_talk.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook/trello")
public class TrelloWebhookController {

    @PostMapping
    public ResponseEntity<String> handleTrelloWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("Ricevuto aggiornamento da Trello: " + payload);
        return ResponseEntity.ok("Webhook Trello ricevuto");
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> handleHeadRequest() {
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        System.err.println("Errore durante la gestione del webhook: " + ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno del server");
    }
}
