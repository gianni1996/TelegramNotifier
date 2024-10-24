package com.trello_talk.trello_talk.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

     /** Path variables **/
     public static final String WORKSPACE_ID = "workspaceId";
     public static final String BOARD_ID = "boardId";
     public static final String LIST_ID = "listId";
     public static final String CARD_ID = "cardId";

     /** Headers **/
     public static final String HEADER_TOKEN = "Token";
     public static final String HEADER_API_KEY = "ApiKey";
     public static final String HEADER_ACCEPT = "Accept";
     public static final String CONTENT_TYPE_JSON = "application/json";

     /** Messages **/
     public static final String GET_URL_MESSAGE = "Invio richiesta GET a: ";
     public static final String RESPONSE_RECEIVED_STATUS = "Risposta ricevuta con status: ";
     public static final String RESPONSE_BODY = "Response body: ";

     public static final String BOARD_GET_MESSAGE = "Recupero i boards";
     public static final String BOARD_GET_REQUEST_MESSAGE = "Richiesta per ottenere tutte le bacheche. IP del client: ";
     public static final String BOARD_CREATE_MESSAGE = "Creazione di un nuovo board con nome: %s per il workspace: ";
     public static final String BOARD_CREATE_REQUEST_MESSAGE = "Richiesta per creare un nuovo board con nome: %s. IP del client: %s";
     public static final String BOARD_DELETE_MESSAGE = "Invio richiesta DELETE per il boardId: ";
     public static final String BOARD_DELETE_MESSAGE_NO_BACKUP = "Il Board non è presente: non è stato backuppato. ID: ";
     public static final String BOARD_DELETE_REQUEST_MESSAGE = "Richiesta per eliminareil board con ID: %s. IP del client: %s";
     public static final String BOARD_DELETED_SUCCESSFULLY = "Board con ID: %s eliminato con successo";
     public static final String BOARD_UPDATE_MESSAGE = "Aggiornamento della board %s con id %s";
     public static final String BOARD_UPDATE_MESSAGE_NO_BACKUP = "Il Board non era presente: di conseguenza non è stato backuppato. ID: ";
     public static final String BOARD_UPDATED_SUCCESSFULLY = "Board con ID: %s aggiornato con successo";
     public static final String BOARD_UPDATE_REQUEST_MESSAGE = "Richiesta di aggiornamento della board ricevuta per il nome: %s, id della board: %s da IP: %s.";
     public static final String BACKUP_BOARDS_START = "Inizio del backup delle board.";
     public static final String BACKUP_BOARDS_SUCCESS = "Backup completato con successo per {} board.";
     public static final String BACKUP_BOARDS_ERROR = "Errore durante il backup delle board: {}";

     public static final String CARD_GET_MESSAGE = "Recupero le carte per la listaId: %s";
     public static final String CARD_GET_REQUEST_MESSAGE = "Richiesta per ottenere tutte le cards per la lista con Id: %s. IP del client: %s";
     public static final String CARD_CREATE_MESSAGE = "Invio richiesta POST per creare una carta con nome: %s e descrizione: %s";
     public static final String CARD_CREATE_REQUEST_MESSAGE = "Richiesta per creare una nuova card con nome: %s. IP del client: %s";
     public static final String CARD_DELETE_MESSAGE = "Invio richiesta DELETE per eliminare la carta con ID: %s";
     public static final String CARD_DELETE_REQUEST_MESSAGE = "Richiesta per eliminare la carta con ID: {}. IP del client: %s";
     public static final String CARD_DELETED_SUCCESSFULLY = "Carta con ID: %s eliminata con successo";
     public static final String CARD_DELETE_MESSAGE_NO_BACKUP = "La card non è presente: di conseguenza non è stata backuppata. ID: ";
     public static final String CARDS_NOT_FOUND_FOR_LIST = "Nessuna card trovata per la listId: ";
     public static final String CARD_UPDATE_MESSAGE_NO_BACKUP = "La card non era presente: di conseguenza non è stata backuppata. ID: ";
     public static final String CARD_UPDATE_MESSAGE = "Aggiornamento della card con nome: %s e descrizione: %s";
     public static final String CARD_UPDATE_REQUEST_MESSAGE = "Richiesta di aggiornamento per la card con ID: %s. IP del client: %s";
     public static final String CARD_UPDATED_SUCCESSFULLY = "Card con ID: %s aggiornata con successo";
     public static final String BACKUP_CARDS_START = "Inizio del backup delle card.";
     public static final String BACKUP_CARDS_SUCCESS = "Backup completato con successo per {} card.";
     public static final String BACKUP_CARDS_ERROR = "Errore durante il backup delle card: {}";

     public static final String LIST_GET_MESSAGE = "Recupero le liste";
     public static final String LIST_GET_REQUEST_MESSAGE = "Richiesta per ottenere tutte le liste per il board con Id: %s. IP del client: %s";
     public static final String LIST_CREATE_REQUEST_MESSAGE = "Richiesta per creare una nuova lista con nome: %s. IP del client: %s";
     public static final String LIST_DELETED_SUCCESSFULLY = "Lista con ID: %s eliminata con successo";
     public static final String LIST_DELETE_REQUEST_MESSAGE = "Richiesta per eliminare la lista con ID: {}. IP del client: %s";
     public static final String LIST_DELETE_MESSAGE_NO_BACKUP = "La lista non è presente: di conseguenza non è stata backuppata. ID: ";
     public static final String LIST_MAP_CARDS_MESSAGE = "Mappo la lista con le sue carte per la listaId: %s";
     public static final String LIST_GET_WITH_CARDS_MESSAGE = "Recupero le liste con le loro carte per il boardId: %s";
     public static final String LIST_UPDATE_REQUEST_MESSAGE = "Richiesta di aggiornamento per la lista con ID: %s. IP del client: %s";
     public static final String LIST_UPDATED_SUCCESSFULLY = "List con ID: %s aggiornata con successo";
     public static final String LIST_UPDATE_MESSAGE_NO_BACKUP = "La list non era presente: di conseguenza non è stato backuppata. ID: ";

     public static final String WORKSPACE_GET_REQUEST_MESSAGE = "Richiesta per ottenere tutti i workspace. IP del client: %s";
     public static final String WORKSPACE_CREATE_REQUEST_MESSAGE = "Richiesta per creare un nuovo workspace con nome: %s. IP del client: %s";
     public static final String WORKSPACE_DELETE_REQUEST_MESSAGE = "Richiesta per eliminare il workspace con ID: %s. IP del client: %s";
     public static final String WORKSPACE_GET_MESSAGE = "Recupero i workspace.";
     public static final String WORKSPACE_CREATE_MESSAGE = "Creazione di un nuovo workspace con nome: %s";
     public static final String WORKSPACE_CREATE_ERROR_MESSAGE = "Errore durante la creazione del workspace con nome: ";
     public static final String WORKSPACE_DELETE_MESSAGE = "Invio richiesta DELETE per il workspaceId: %s";
     public static final String WORKSPACE_DELETED_SUCCESSFULLY = "Workspace con ID: %s eliminato con successo.";
     public static final String WORKSPACE_UPDATE_REQUEST_MESSAGE = "Richiesta di aggiornamento per il workspace con ID: %s. IP del client: %s";
     public static final String WORKSPACE_UPDATE_MESSAGE_NO_BACKUP = "Il workspace non era presente: di conseguenza non è stato backuppata. ID: ";
     public static final String WORKSPACE_UPDATED_SUCCESSFULLY = "Workspace con ID: %s aggiornato con successo";
     public static final String BACKUP_LISTS_START = "Inizio del backup delle liste.";
     public static final String BACKUP_LISTS_SUCCESS = "Backup completato con successo per {} liste.";
     public static final String BACKUP_LISTS_ERROR = "Errore durante il backup delle liste: {}";

     /** Error Messages **/
     public static final String ERROR_REQUEST_MESSAGE = "Errore nella richiesta: ";

     public static final String BOARD_GET_ERROR_MESSAGE = "Errore nel recupero dei boards";
     public static final String BOARD_CREATE_ERROR_MESSAGE = "Errore durante la creazione del board con nome: ";
     public static final String BOARD_DELETE_ERROR_MESSAGE = "Errore durante l'eliminazione del board con ID: ";
     public static final String BOARD_UPDATE_ERROR_MESSAGE = "Errore durante l'aggiornamento della board con id: ";
     public static final String BOARD_NOT_FOUND = "Nessun board trovato.";
     public static final String BOARD_ERROR_TIMEOUT =  "Timeout durante il recupero dei boards con workspace con id: ";

     public static final String CARD_GET_ERROR_MESSAGE = "Errore nel recupero delle card per la listaId: ";
     public static final String CARD_CREATE_ERROR_MESSAGE = "Errore durante la creazione della carta";
     public static final String CARD_DELETE_ERROR_MESSAGE = "Errore durante l'eliminazione della carta con ID: ";
     public static final String CARD_UPDATE_ERROR_MESSAGE = "Errore durante l'aggiornamento della card";
     public static final String CARD_ERROR_TIMEOUT =  "Timeout durante il recupero delle card per la lista con id: ";

     public static final String LIST_CREATE_ERROR_MESSAGE = "Errore durante la creazione della lista con nome: ";
     public static final String LIST_GET_ERROR_MESSAGE = "Errore nel recupero delle liste";
     public static final String LIST_DELETE_ERROR_MESSAGE = "Errore durante l'eliminazione della lista con ID: ";
     public static final String LIST_UPDATE_ERROR_MESSAGE = "Errore durante l'aggiornamento della lista";
     public static final String LIST_ERROR_TIMEOUT =  "Timeout durante il recupero delle liste con board con id: ";

     public static final String WORKSPACE_NOT_FOUND = "Nessun workspace trovato.";
     public static final String WORKSPACE_GET_ERROR_MESSAGE = "Errore nel recupero dei workspace.";
     public static final String WORKSPACE_DELETE_ERROR_MESSAGE = "Errore durante l'eliminazione del workspace con ID: ";
     public static final String WORKSPACE_UPDATE_ERROR_MESSAGE = "Errore durante l'aggiornamento del workspace";
     public static final String WORKSPACE_ERROR_TIMEOUT =  "Timeout durante il recupero dei workspaces ";
}
