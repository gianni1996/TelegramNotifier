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
public class CardApiConfig {

    @Value("${api.trello.card.get}")
    private String getCardsUrl;

    @Value("${api.trello.card.create}")
    private String createCardUrl;

    @Value("${api.trello.card.delete}")
    private String deleteCardUrl;

    @Value("${api.trello.card.update}")
    private String updateCardUrl;
}