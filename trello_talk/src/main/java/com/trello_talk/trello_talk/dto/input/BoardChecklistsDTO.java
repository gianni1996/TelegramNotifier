package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardChecklistsDTO {
    private BoardChecklistLimitsDTO perBoard;
    private BoardChecklistLimitsDTO perCard; 
}
