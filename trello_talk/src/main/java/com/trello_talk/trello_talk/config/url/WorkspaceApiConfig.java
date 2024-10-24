package com.trello_talk.trello_talk.config.url;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceApiConfig {

    @Value("${api.trello.workspace.get}")
    private String getWorkspacesUrl;
    @Value("${api.trello.workspace.create}")
    private String createWorkspaceUrl;
    @Value("${api.trello.workspace.delete}")
    private String deleteWorkspaceUrl;
    @Value("${api.trello.workspace.update}")
    private String updateWorkspaceUrl;
}
