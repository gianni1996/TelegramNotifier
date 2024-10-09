package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardInputDTO {
    private String id;
    private String name;
    private String desc;
    private String idList;
    private String idBoard;
    private String due;
    private Boolean closed;
    private Integer pos;
    private String labels;
    private String url;
    private String[] attachments;
}


