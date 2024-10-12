package com.trello_talk.trello_talk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrelloBoardApiConfig {
    @Value("${api.trello.board.get}")
    private String getBoardsUrl;
    @Value("${api.trello.board.create}")
    private String createBoardUrl;
    @Value("${api.trello.board.delete}")
    private String deleteBoardUrl;
}
