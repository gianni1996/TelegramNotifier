package com.telegram_notifier.telegram_notifier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telegram_notifier.telegram_notifier.config.TelegramConfig;
import com.telegram_notifier.telegram_notifier.config.TrelloConfig;
import com.telegram_notifier.telegram_notifier.model.TrelloCard;
import com.telegram_notifier.telegram_notifier.model.TrelloList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MiddleService {

    @Autowired
    private TrelloService trelloService;
    @Autowired
    private TrelloConfig trelloConfig;
    @Autowired
    private TelegramConfig telegramConfig;
    @Autowired
    private TelegramService telegramService;

    public void downloadLists() {

        List<TrelloList> lists = trelloService.getLists(trelloConfig.getBoardId());
        StringBuilder messageBuilder = new StringBuilder();

        if (isListWithoutLists(lists)) {
            messageBuilder.append("Nessuna lista presente. \n");
            return;
        }

        log.info("Starting Lists Download. ");

        for (TrelloList list : lists) {
            List<TrelloCard> cards = trelloService.getCardsInList(list.getId());
            log.info("cards in list {}: {}", list.getName(), cards);
            messageBuilder.append("List Name: ").append(list.getName()).append("\n");

            if (isListWithoutCards(cards)) {
                // Se non ci sono carte, non aggiungere nulla
                messageBuilder.append("    Nessuna attività presente. \n");
                continue;
            }

            for (TrelloCard card : cards) {
                messageBuilder.append("    - ").append(card.getName()).append("\n");
            }
        }

        telegramService.sendMessage(telegramConfig.getChatid(), messageBuilder.toString());

        log.info("End of Lists Download. ");
    }

    private boolean isListWithoutCards(List<TrelloCard> cards) {
        return cards == null || cards.isEmpty() || cards.stream().allMatch(card -> card.getId().isBlank());
    }

    private boolean isListWithoutLists(List<TrelloList> lists) {
        return lists == null || lists.isEmpty() || lists.stream().allMatch(list -> list.getId().isBlank());
    }

}
