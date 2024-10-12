package com.trello_talk.trello_talk.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

     /** Path variables **/
     public static final String WORKSPACE_ID="workspaceId";
     public static final String BOARD_ID="boardId";
     public static final String LIST_ID="listId";
     public static final String CARD_ID="cardId";

     /** Headers **/
     public static final String HEADER_TOKEN = "Token";
     public static final String HEADER_API_KEY = "ApiKey";
     public static final String HEADER_ACCEPT = "Accept";
     public static final String CONTENT_TYPE_JSON = "application/json";

     /** Messages **/
     public static final String GET_URL_MESSAGE = "Invio richiesta GET a: %s";
     public static final String RESPONSE_RECEIVED_STATUS = "Risposta ricevuta con status: %d";
     public static final String RESPONSE_BODY = "Response body: %s";
     public static final String BOARD_GET_MESSAGE = "Recupero i boards.";
     public static final String BOARD_GET_REQUEST_MESSAGE = "Richiesta per ottenere tutte le bacheche. IP del client: %s";
     public static final String BOARD_CREATE_MESSAGE = "Creazione di un nuovo board con nome: %s per il workspace: ";
     public static final String BOARD_CREATE_REQUEST_MESSAGE = "Richiesta per creare un nuovo board con nome: %s. IP del client: %s";
     public static final String BOARD_DELETE_MESSAGE = "Invio richiesta DELETE per il boardId: ";
     public static final String BOARD_DELETE_REQUEST_MESSAGE = "Richiesta per eliminareil board con ID: %s. IP del client: %s";
     public static final String BOARD_DELETED_SUCCESSFULLY = "Board con ID: %s eliminato con successo.";

     /** Error Messages **/
     public static final String ERROR_REQUEST_MESSAGE = "Errore nella richiesta: ";
     public static final String BOARD_GET_ERROR_MESSAGE = "Errore nel recupero dei boards";
     public static final String BOARD_CREATE_ERROR_MESSAGE = "Errore durante la creazione del board con nome: ";
     public static final String BOARD_DELETE_ERROR_MESSAGE = "Errore durante l'eliminazione del board con ID: ";
     public static final String BOARD_NOT_FOUND = "Nessun board trovato.";
}
     
