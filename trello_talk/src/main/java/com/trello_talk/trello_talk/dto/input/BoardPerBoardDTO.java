package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardPerBoardDTO {
    private String status;
    private Integer disableAt;
    private Integer warnAt; 
}