package com.trello_talk.trello_talk.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Action {
    CREATE("Create"),
    GET("Get"),
    DELETE("Delete");

    private final String name;
}