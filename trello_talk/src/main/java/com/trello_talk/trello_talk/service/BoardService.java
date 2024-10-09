package com.trello_talk.trello_talk.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trello_talk.trello_talk.config.error.ApiException;
import com.trello_talk.trello_talk.dto.input.BoardInputDTO;
import com.trello_talk.trello_talk.dto.output.BoardOutputDTO;
import com.trello_talk.trello_talk.dto.response.BoardListResponse;
import com.trello_talk.trello_talk.dto.response.BoardResponse;
import com.trello_talk.trello_talk.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    public BoardListResponse getAllBoards(String token, String apiKey) {
        log.info("Recupero i boards.");
        try {
            List<BoardInputDTO> boards = getBoards(token, apiKey);

            if (boards == null || boards.isEmpty()) {
                log.error("Nessun board trovato.");
                return new BoardListResponse(List.of());
            }

            List<BoardOutputDTO> boardOutputDTOs = boards.stream()
                    .map(boardMapper::toOutputDto)
                    .collect(Collectors.toList());

            return new BoardListResponse(boardOutputDTOs);

        } catch (Exception e) {
            log.error("Errore nel recupero dei boards", e);
            throw new ApiException("Errore nel recupero dei boards", e);
        }
    }

    protected List<BoardInputDTO> getBoards(String token, String apiKey) {
        String url = String.format("https://api.trello.com/1/members/me/boards?key=%s&token=%s", apiKey, token);
        log.info("Invio richiesta GET a: {}", url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            log.info("Risposta ricevuta con stato: {}", response.statusCode());
            log.info("Corpo della risposta: {}", response.body());

            if (response.statusCode() != 200) {
                throw new ApiException("Errore nella richiesta: " + response.statusCode());
            }

            return objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, BoardInputDTO.class));

        } catch (Exception e) {
            log.error("Errore durante la comunicazione con Trello", e);
            throw new ApiException("Errore durante la comunicazione con Trello", e);
        }
    }

    public BoardResponse createBoard(String name, String idWorkspace, String token, String apiKey) {
        String url = String.format("https://api.trello.com/1/boards/?name=%s&idOrganization=%s&key=%s&token=%s",
                name, idWorkspace, apiKey, token);
        log.info("Creazione di un nuovo board con nome: {} per il workspace: {}", name, idWorkspace);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            log.info("Risposta ricevuta con stato: {}", response.statusCode());
            log.info("Corpo della risposta: {}", response.body());

            if (response.statusCode() != 200) {
                throw new ApiException("Errore nella richiesta: " + response.statusCode());
            }

            BoardOutputDTO boardOutputDTO = objectMapper.readValue(response.body(), BoardOutputDTO.class);

            return new BoardResponse(boardOutputDTO);

        } catch (Exception e) {
            log.error("Errore durante la creazione del board con nome: " + name, e);
            throw new ApiException("Errore durante la creazione del board con nome: " + name, e);
        }
    }

    public void deleteBoard(String boardId, String apiKey, String token) {
        String url = String.format("https://api.trello.com/1/boards/%s?key=%s&token=%s", boardId, apiKey, token);
        log.info("Invio richiesta DELETE per il boardId: {}", boardId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            log.info("Risposta ricevuta con stato: {}", response.statusCode());

            if (response.statusCode() != 200) {
                throw new ApiException("Errore nella richiesta DELETE per il board: " + response.statusCode());
            }

            log.info("Board con ID: {} eliminato con successo.", boardId);

        } catch (Exception e) {
            log.error("Errore durante l'eliminazione del board con ID: " + boardId, e);
            throw new ApiException("Errore durante l'eliminazione del board con ID: " + boardId, e);
        }
    }
}
