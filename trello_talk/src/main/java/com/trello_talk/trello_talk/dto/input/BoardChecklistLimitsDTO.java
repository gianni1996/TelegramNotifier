package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardChecklistLimitsDTO {
    private String status;    
    private int disableAt; 
    private int warnAt; 
}
