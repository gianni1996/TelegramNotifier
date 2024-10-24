package com.trello_talk.trello_talk.dto.output;

import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardOutputDTO {
   
    private String id;
    private String name;
    private String description;
    private String idList;
    private String idBoard;
    private Calendar due;
    private Boolean closed;
    private Integer position;
    private String labels;
    private String url;
}
