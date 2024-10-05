package com.trello_talk.trello_talk.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.input.BoardInputDTO;
import com.trello_talk.trello_talk.dto.output.BoardOutputDTO;
import com.trello_talk.trello_talk.mapper.BoardMapper;
import com.trello_talk.trello_talk.util.HttpRequestUtil;
import com.trello_talk.trello_talk.util.JsonParserUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardService {

    @Autowired
    private BoardMapper boardMapper;

    public List<BoardOutputDTO> getAllBoards(String token, String apiKey) {
        log.info("Recupero i boards. ");
        try {
            List<BoardInputDTO> lists = getBoards(token, apiKey);

            if (lists == null || lists.isEmpty()) {
                throw new ApiException("No boards found for this user. ");
            }

            return lists.stream()
                    .map(boardMapper::toOutputDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Errore nel recupero dei boards ", e);
            throw new ApiException("Errore nel recupero dei boards ", e);
        }
    }

    protected List<BoardInputDTO> getBoards(String token, String apiKey) {
        String url = "https://api.trello.com/1/members/me/boards?key=" + apiKey + "&token=" + token;
        log.info("Invio richiesta GET a: {}", url);
        String jsonResponse = HttpRequestUtil.sendGetRequest(url);
        log.info("Risposta ricevuta: {}", jsonResponse);
        return JsonParserUtil.parseJsonToList(jsonResponse, new TypeReference<List<BoardInputDTO>>() {
        });
    }

}
