package com.trello_talk.trello_talk.dto.response;

import com.trello_talk.trello_talk.dto.output.TrelloListOutputDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrelloListResponse {
    private TrelloListOutputDTO list;
}