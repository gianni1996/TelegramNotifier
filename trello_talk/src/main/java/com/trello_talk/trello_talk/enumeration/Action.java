package com.trello_talk.trello_talk.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Action {
    CREATE("Create"),
    GET("Get"),
    DELETE("Delete"),
    UPDATE("Update"),
    BACKUP("Backup");

    private final String name;
}