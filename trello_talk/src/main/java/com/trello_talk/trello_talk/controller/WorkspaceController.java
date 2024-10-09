package com.trello_talk.trello_talk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trello_talk.trello_talk.dto.request.WorkspaceCreateRequest;
import com.trello_talk.trello_talk.dto.response.WorkspaceListResponse;
import com.trello_talk.trello_talk.dto.response.WorkspaceResponse;
import com.trello_talk.trello_talk.service.WorkspaceService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/workspace")
@Slf4j
public class WorkspaceController {
    @Autowired
    WorkspaceService workspaceService;

    @GetMapping("/getall")
    public ResponseEntity<WorkspaceListResponse> getAllWorkspaces(
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        WorkspaceListResponse workspaces = workspaceService.getAllWorkspaces(token, apiKey);
        return ResponseEntity.ok(workspaces);
    }

    @PostMapping
    public ResponseEntity<WorkspaceResponse> createWorkspace(
            @RequestBody WorkspaceCreateRequest workspaceCreateRequestDTO,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        String name = workspaceCreateRequestDTO.getName();
        log.info("Richiesta per creare un nuovo workspace con nome: {}", name);
        WorkspaceResponse workspace = workspaceService.createWorkspace(name, token, apiKey);

        return ResponseEntity.ok(workspace);
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(
            @PathVariable("workspaceId") String workspaceId,
            @RequestHeader("Token") String token,
            @RequestHeader("ApiKey") String apiKey) {

        log.info("Richiesta per eliminare il workspace con ID: {}", workspaceId);
        workspaceService.deleteWorkspace(workspaceId, apiKey, token);

        return ResponseEntity.noContent().build();
    }
}
