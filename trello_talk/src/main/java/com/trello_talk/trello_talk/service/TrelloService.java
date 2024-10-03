package com.trello_talk.trello_talk.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.input.TrelloCardInputDTO;
import com.trello_talk.trello_talk.dto.input.TrelloListInputDTO;
import com.trello_talk.trello_talk.dto.output.TrelloCardOutputDTO;
import com.trello_talk.trello_talk.dto.output.TrelloListOutputDTO;
import com.trello_talk.trello_talk.dto.output.TrelloListWithCardsDTO;
import com.trello_talk.trello_talk.mapper.TrelloCardMapper;
import com.trello_talk.trello_talk.mapper.TrelloListMapper;
import com.trello_talk.trello_talk.repository.TrelloRepository;
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

    // Metodo per recuperare le liste con le loro carte per un determinato boardId
    public List<TrelloListWithCardsDTO> getListsWithCards(String boardId) {
        log.info("Recupero le liste con le loro carte per il boardId: {}", boardId);
        List<TrelloListOutputDTO> lists = getLists(boardId);
        return lists.stream()
                .map(this::mapListWithCards) // Mappa ogni lista con le sue carte
                .collect(Collectors.toList());
    }

    // Metodo privato per recuperare le liste dal repository
    public List<TrelloListOutputDTO> getLists(String boardId) {
        log.info("Recupero le liste per il boardId: {}", boardId);
        try {
            List<TrelloListInputDTO> lists = trelloRepository.getListsFromBoard(boardId);

            if (lists == null || lists.isEmpty()) {
                throw new ApiException("No lists found for board ID: " + boardId);
            }

            // Directly return the mapped list
            return lists.stream()
                    .map(trelloListMapper::toOutputDto) // Use the mapper to convert to output DTO
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Errore nel recupero delle liste per il boardId: " + boardId, e);
            throw new ApiException("Unable to retrieve lists for board ID: " + boardId, e);
        }
    }
    

    // Metodo privato per recuperare le carte per una lista specifica
    public List<TrelloCardOutputDTO> getCards(String listId) {
        log.info("Recupero le carte per la listaId: {}", listId);
        try {
            List<TrelloCardInputDTO> cards = trelloRepository.getCardsFromList(listId);

            if (cards == null || cards.isEmpty()) {
                return List.of();
            }

            List<TrelloCardOutputDTO> outputList = cards.stream()
                .map(trelloCardMapper::toOutputDto) // Use the mapper to convert to output DTO
                .collect(Collectors.toList());

            return outputList; // Restituisce una lista vuota se non ci sono carte
        } catch (Exception e) {
            log.error("Errore nel recupero delle card per la listaId: " + listId, e);
            throw new ApiException("Unable to retrieve cards for list ID: " + listId, e);
        }
    }

    // Metodo privato per mappare una lista e le sue carte in un DTO
    private TrelloListWithCardsDTO mapListWithCards(TrelloListOutputDTO list) {
        log.info("Mappo la lista con le sue carte per la listaId: {}", list.getId());
        List<TrelloCardOutputDTO> cards = getCards(list.getId()); // Recupera le carte per la lista
        return new TrelloListWithCardsDTO(list, cards); // Restituisce il DTO con lista e carte
    }

}

