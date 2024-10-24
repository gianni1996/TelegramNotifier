package com.trello_talk.trello_talk.config.url;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardApiConfig {
    @Value("${api.trello.board.get}")
    private String getBoardsUrl;
    @Value("${api.trello.board.create}")
    private String createBoardUrl;
    @Value("${api.trello.board.delete}")
    private String deleteBoardUrl;
    @Value("${api.trello.board.update}")
    private String updateBoardUrl;
}
