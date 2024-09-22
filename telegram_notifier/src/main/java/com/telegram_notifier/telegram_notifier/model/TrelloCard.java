package com.telegram_notifier.telegram_notifier.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrelloCard {
    private String id;             // ID della card
    private String name;           // Nome della card
    private String desc;           // Descrizione della card
    private String idList;         // ID della lista a cui appartiene
    private String idBoard;        // ID della board a cui appartiene
    private String due;            // Scadenza della card, se disponibile
    private boolean closed;        // Se la card è chiusa
    private int pos;               // Posizione della card nella lista
    private String labels;         // Etichette della card, se disponibili
    private String url;            // URL della card
    private String[] attachments;  // Allegati, se disponibili
}
