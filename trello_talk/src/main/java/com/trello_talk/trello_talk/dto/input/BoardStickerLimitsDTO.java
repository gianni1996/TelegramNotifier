package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardStickerLimitsDTO {
    private String status;    
    private int disableAt;   
    private int warnAt;  
}
