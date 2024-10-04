package com.trello_talk.trello_talk.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListWithCardsDTO {
    List<ListWithCardsDTO> trelloListWithCards;
}
