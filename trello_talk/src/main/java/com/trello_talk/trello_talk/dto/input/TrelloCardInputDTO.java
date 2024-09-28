package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrelloCardInputDTO {
    private String id;
    private String name;
    private String desc;
    private String idList;
    private String idBoard;
    private String due;
    private boolean closed;
    private int pos;
    private String labels;
    private String url;
    private String[] attachments;
}


