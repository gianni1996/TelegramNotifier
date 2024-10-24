package com.trello_talk.trello_talk.dto.response;

import java.util.List;

import com.trello_talk.trello_talk.dto.output.WorkspaceWithCardsDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceListResponse {
    private List<WorkspaceWithCardsDTO> workspaces;
}
