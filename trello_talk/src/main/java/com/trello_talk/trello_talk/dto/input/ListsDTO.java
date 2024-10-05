package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListsDTO{
    public OpenPerBoardDTO openPerBoard;
    public TotalPerBoardDTO totalPerBoard;
}
