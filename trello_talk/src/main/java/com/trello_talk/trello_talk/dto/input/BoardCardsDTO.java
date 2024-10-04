package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCardsDTO {
    private BoardCardLimitsDTO openPerBoard;
    private BoardCardLimitsDTO openPerList;
    private BoardCardLimitsDTO totalPerBoard;
    private BoardCardLimitsDTO totalPerList;
}
