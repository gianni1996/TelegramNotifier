package com.trello_talk.trello_talk.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrelloListOutputDTO {

    private String id;
    private String name;
    private Boolean closed;
    private String idBoard;
    private Integer position;
    private Boolean subscribed;
    private String color;
    private String softLimit;
    private String type;
}
