package com.trello_talk.trello_talk.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrelloListWithCardsDTO {
    private TrelloListOutputDTO trelloList;
    private List<TrelloCardOutputDTO> cards;
}
