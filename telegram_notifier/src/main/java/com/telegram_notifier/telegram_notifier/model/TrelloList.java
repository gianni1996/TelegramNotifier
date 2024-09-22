package com.telegram_notifier.telegram_notifier.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrelloList {
    private String id;             // ID della lista
    private String name;           // Nome della lista
    private boolean closed;        // Se la lista è chiusa
    private String idBoard;        // ID della board a cui appartiene
    private int pos;               // Posizione della lista
    private boolean subscribed;    // Se ci si è iscritti alle notifiche
    private String color;          // Colore della lista, se disponibile
    private String softLimit;      // Limite morbido, se disponibile
    private String type;           // Tipo di lista, se disponibile
    private Object datasource;     // DB, se disponibile
}
