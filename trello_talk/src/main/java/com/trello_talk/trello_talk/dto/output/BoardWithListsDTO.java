package com.trello_talk.trello_talk.dto.output;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardWithListsDTO {
    BoardOutputDTO board;
    List<ListWithCardsDTO> lists;
}
