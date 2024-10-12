package com.trello_talk.trello_talk.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WorkItem {
    WORKSPACE("Workspace"),
    BOARD("Board"),
    LIST("List"),
    CARD("Card");

    private final String name;
}