package com.telegram_notifier.telegram_notifier.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.telegram_notifier.telegram_notifier.model.TrelloCard;
import com.telegram_notifier.telegram_notifier.model.TrelloList;
import com.telegram_notifier.telegram_notifier.service.TrelloService;

import java.util.List;

@RestController
public class TrelloController {

    @Autowired
    private TrelloService trelloService;

    @GetMapping("/boards/{boardId}/lists")
    public List<TrelloList> getLists(@PathVariable String boardId) {
        return trelloService.getLists(boardId);
    }

    @GetMapping("/lists/{listId}/cards")
    public List<TrelloCard> getCards(@PathVariable String listId) {
        return trelloService.getCardsInList(listId);
    }
}
