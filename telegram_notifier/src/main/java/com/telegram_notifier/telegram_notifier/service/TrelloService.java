package com.telegram_notifier.telegram_notifier.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telegram_notifier.telegram_notifier.dto.input.TrelloCardInputDTO;
import com.telegram_notifier.telegram_notifier.dto.input.TrelloListInputDTO;
import com.telegram_notifier.telegram_notifier.mapper.TrelloCardMapper;
import com.telegram_notifier.telegram_notifier.mapper.TrelloListMapper;
import com.telegram_notifier.telegram_notifier.model.TrelloCard;
import com.telegram_notifier.telegram_notifier.model.TrelloList;
import com.telegram_notifier.telegram_notifier.repository.TrelloRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrelloService {

    @Autowired
    private TrelloRepository trelloRepository;

    @Autowired
    private TrelloListMapper trelloListMapper;

    @Autowired
    private TrelloCardMapper trelloCardMapper;

    public List<TrelloList> getLists(String boardId) {
        List<TrelloListInputDTO> lists = null;
        try {
            lists = trelloRepository.getListsFromBoard(boardId);
        } catch (Exception e) {
            log.error("Errore nel recupero delle liste", e);
        }
        return lists == null ? null : mapListsToModel(lists);
    }

    private List<TrelloList> mapListsToModel(List<TrelloListInputDTO> lists) {
        return lists.stream().map(trelloListMapper::toModel).collect(Collectors.toList());
    }

    public List<TrelloCard> getCardsInList(String listId) {
        List<TrelloCardInputDTO> cards = null;
        try {
            cards = trelloRepository.getCardsFromList(listId);
        } catch (Exception e) {
            log.error("Errore nel recupero delle card", e);
        }
        return cards == null ? null : mapCardsToModel(cards);
    }

    private List<TrelloCard> mapCardsToModel(List<TrelloCardInputDTO> cards) {
        return cards.stream().map(trelloCardMapper::toModel).collect(Collectors.toList());
    }
}

