package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDetailsDTO{
    public OpenPerBoardDTO openPerBoard;
    public OpenPerListDTO openPerList;
    public TotalPerBoardDTO totalPerBoard;
    public TotalPerListDTO totalPerList;
}
