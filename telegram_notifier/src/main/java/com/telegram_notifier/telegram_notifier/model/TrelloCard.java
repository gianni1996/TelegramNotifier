package com.telegram_notifier.telegram_notifier.model;

public class TrelloCard {
    private String id;
    private String name;
    private String description; // Aggiunta del campo per la descrizione

    public TrelloCard(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description; 
    }

    @Override
    public String toString() {
        return "TrelloCard{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
