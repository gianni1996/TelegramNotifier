package com.telegram_notifier.telegram_notifier.model;

public class TrelloList {
    private String id;
    private String name;

    public TrelloList(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TrelloList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
