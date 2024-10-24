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
public class TrelloListApiConfig {

    @Value("${api.trello.list.get}")
    private String getListsUrl;
    @Value("${api.trello.list.create}")
    private String createListUrl;
    @Value("${api.trello.list.delete}")
    private String deleteListUrl;
    @Value("${api.trello.list.update}")
    private String updateListUrl;
}
