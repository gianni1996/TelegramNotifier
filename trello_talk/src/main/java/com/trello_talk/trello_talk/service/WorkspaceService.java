package com.trello_talk.trello_talk.service;

import java.net.URI;
import java.net.URLEncoder;
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
import com.trello_talk.trello_talk.dto.input.WorkspaceInputDTO;
import com.trello_talk.trello_talk.dto.output.WorkspaceOutputDTO;
import com.trello_talk.trello_talk.dto.response.WorkspaceListResponse;
import com.trello_talk.trello_talk.dto.response.WorkspaceResponse;
import com.trello_talk.trello_talk.mapper.WorkspaceMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WorkspaceService {

    @Autowired
    private WorkspaceMapper workspaceMapper;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    public WorkspaceListResponse getAllWorkspaces(String token, String apiKey) {
        log.info("Recupero i workspace.");
        try {
            List<WorkspaceInputDTO> workspaces = getWorkspaces(token, apiKey);

            if (workspaces == null || workspaces.isEmpty()) {
                log.error("Nessun workspace trovato.");
                return new WorkspaceListResponse(List.of());
            }

            List<WorkspaceOutputDTO> workspaceOutputDTOs = workspaces.stream()
                    .map(workspaceMapper::toOutputDto)
                    .collect(Collectors.toList());

            return new WorkspaceListResponse(workspaceOutputDTOs);

        } catch (Exception e) {
            log.error("Errore nel recupero dei workspace", e);
            throw new ApiException("Errore nel recupero dei workspace", e);
        }
    }

    protected List<WorkspaceInputDTO> getWorkspaces(String token, String apiKey) {
        try {
            String url = String.format("https://api.trello.com/1/members/me/organizations?key=%s&token=%s", apiKey,
                    token);
            log.info("Invio richiesta GET a: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            log.info("Risposta ricevuta con stato: {}", response.statusCode());
            log.info("Corpo della risposta: {}", response.body());

            if (response.statusCode() != 200) {
                throw new ApiException("Errore nella richiesta: " + response.statusCode());
            }

            return objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, WorkspaceInputDTO.class));

        } catch (Exception e) {
            log.error("Errore durante la comunicazione con Trello", e);
            throw new ApiException("Errore durante la comunicazione con Trello", e);
        }
    }

    public WorkspaceResponse createWorkspace(String name, String token, String apiKey) {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
            String url = String.format("https://api.trello.com/1/organizations?displayName=%s&key=%s&token=%s",
                    encodedName,
                    apiKey, token);
            log.info("Creazione di un nuovo workspace con nome: {}", name);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            log.info("Risposta ricevuta con stato: {}", response.statusCode());
            log.info("Corpo della risposta: {}", response.body());

            if (response.statusCode() != 200) {
                throw new ApiException("Errore nella richiesta: " + response.statusCode());
            }

            WorkspaceOutputDTO workspaceOutputDTO = objectMapper.readValue(response.body(), WorkspaceOutputDTO.class);

            return new WorkspaceResponse(workspaceOutputDTO);

        } catch (Exception e) {
            log.error("Errore durante la creazione del workspace con nome: " + name, e);
            throw new ApiException("Errore durante la creazione del workspace con nome: " + name, e);
        }
    }

    public void deleteWorkspace(String workspaceId, String apiKey, String token) {
        try {
            String url = String.format("https://api.trello.com/1/organizations/%s?key=%s&token=%s", workspaceId, apiKey,
                    token);
            log.info("Invio richiesta DELETE per il workspaceId: {}", workspaceId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            log.info("Risposta ricevuta con stato: {}", response.statusCode());

            if (response.statusCode() != 200) {
                throw new ApiException("Errore nella richiesta DELETE per il workspace: " + response.statusCode());
            }

            log.info("Workspace con ID: {} eliminato con successo.", workspaceId);

        } catch (Exception e) {
            log.error("Errore durante l'eliminazione del workspace con ID: " + workspaceId, e);
            throw new ApiException("Errore durante l'eliminazione del workspace con ID: " + workspaceId, e);
        }
    }
}
