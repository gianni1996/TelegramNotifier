package com.telegram_notifier.telegram_notifier.service;

import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import com.telegram_notifier.telegram_notifier.config.TelegramConfig;
import com.telegram_notifier.telegram_notifier.config.TrelloConfig;
import com.telegram_notifier.telegram_notifier.model.TrelloCard;
import com.telegram_notifier.telegram_notifier.model.TrelloList;
import com.telegram_notifier.telegram_notifier.util.DiagramGenerator;
import org.jfree.data.general.DefaultPieDataset; 

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

        DefaultPieDataset dataset = new DefaultPieDataset();
        log.info("Starting Lists Download. ");

        for (TrelloList list : lists) {
            List<TrelloCard> cards = trelloService.getCardsInList(list.getId());
            log.info("cards in list {}: {}", list.getName(), cards);

            appendCardsToMessageBuilder(cards, messageBuilder, list.getName());

            int cardCount = cards.size();
            dataset.setValue(list.getName(), cardCount);
        }

        sendChartToTelegram(dataset);
        telegramService.sendMessage(telegramConfig.getChatid(), messageBuilder.toString());
        log.info("End of Lists Download. ");
    }

    private void sendChartToTelegram(DefaultPieDataset dataset) {
        File chartFile = DiagramGenerator.generateChart(dataset);
        InputFile inputFile = new InputFile(chartFile);

        if (chartFile != null) {
            telegramService.sendPhoto(telegramConfig.getChatid(), inputFile);
        }    
    }

    private void appendCardsToMessageBuilder(List<TrelloCard> cards, StringBuilder messageBuilder, String listName) {
        messageBuilder.append("*").append(listName).append("*:\n"); // Nome della lista in grassetto
        
        if(isListWithoutCards(cards)){
            messageBuilder.append("      Nessuna card presente. ").append("\n");
            return;
        };

        for (TrelloCard card : cards) {
            messageBuilder.append("    • ").append(card.getName()).append("\n"); // Utilizza un punto elenco
            if (card.getDesc() != null && !card.getDesc().isEmpty()) {
                messageBuilder.append("      → ").append(card.getDesc()).append("\n"); // Indentazione per la descrizione
            }
        }
    }

    private boolean isListWithoutCards(List<TrelloCard> cards) {
        return cards == null || cards.isEmpty() || cards.stream().allMatch(card -> card.getId().isBlank());
    }

    private boolean isListWithoutLists(List<TrelloList> lists) {
        return lists == null || lists.isEmpty() || lists.stream().allMatch(list -> list.getId().isBlank());
    }
}
