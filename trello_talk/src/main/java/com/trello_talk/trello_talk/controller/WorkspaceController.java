package com.trello_talk.trello_talk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trello_talk.trello_talk.dto.request.WorkspaceCreateRequest;
import com.trello_talk.trello_talk.dto.request.WorkspaceUpdateRequest;
import com.trello_talk.trello_talk.dto.response.WorkspaceListResponse;
import com.trello_talk.trello_talk.dto.response.WorkspaceResponse;
import com.trello_talk.trello_talk.service.WorkspaceService;
import com.trello_talk.trello_talk.util.Constants;
import com.trello_talk.trello_talk.util.IpTracker;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/workspace")
@Slf4j
public class WorkspaceController {

        @Autowired
        private WorkspaceService workspaceService;
        @Autowired
        private IpTracker ipTracker;

        @GetMapping("/getall")
        public Mono<ResponseEntity<WorkspaceListResponse>> getAllWorkspaces(
                        @RequestHeader(Constants.HEADER_TOKEN) String token,
                        @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
                        HttpServletRequest request) {

                String clientIp = ipTracker.getClientIp(request);
                log.info(Constants.WORKSPACE_GET_REQUEST_MESSAGE, clientIp);

                return workspaceService.getAllWorkspaces(token, apiKey, clientIp)
                                .map(ResponseEntity::ok);
        }

        @PostMapping("/create")
        public Mono<ResponseEntity<WorkspaceResponse>> createWorkspace(
                        @RequestBody WorkspaceCreateRequest workspaceCreateRequestDTO,
                        @RequestHeader(Constants.HEADER_TOKEN) String token,
                        @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
                        HttpServletRequest request) {

                String name = workspaceCreateRequestDTO.getName();
                String clientIp = ipTracker.getClientIp(request);
                log.info(Constants.WORKSPACE_CREATE_REQUEST_MESSAGE, name, clientIp);

                return workspaceService.createWorkspace(name, token, apiKey, clientIp)
                                .map(ResponseEntity::ok);
        }

        @DeleteMapping("/delete/{workspaceId}")
        public Mono<ResponseEntity<Void>> deleteWorkspace(
                        @PathVariable(Constants.WORKSPACE_ID) String workspaceId,
                        @RequestHeader(Constants.HEADER_TOKEN) String token,
                        @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
                        HttpServletRequest request) {

                String clientIp = ipTracker.getClientIp(request);
                log.info(Constants.WORKSPACE_DELETE_REQUEST_MESSAGE, workspaceId, clientIp);

                return workspaceService.deleteWorkspace(workspaceId, apiKey, token, clientIp)
                                .map(v -> ResponseEntity.noContent().build());
        }

        @PutMapping("/update/{workspaceId}")
        public Mono<ResponseEntity<WorkspaceResponse>> updateWorkspace(
                        @PathVariable(Constants.WORKSPACE_ID) String workspaceId,
                        @RequestBody WorkspaceUpdateRequest updateWorkspaceRequest,
                        @RequestHeader(Constants.HEADER_TOKEN) String token,
                        @RequestHeader(Constants.HEADER_API_KEY) String apiKey,
                        HttpServletRequest request) {

                String name = updateWorkspaceRequest.getName();
                String clientIp = ipTracker.getClientIp(request);
                log.info(Constants.WORKSPACE_UPDATE_REQUEST_MESSAGE, workspaceId, clientIp);

                return workspaceService.updateWorkspace(workspaceId, name, token, apiKey, clientIp)
                                .map(ResponseEntity::ok);
        }
}
