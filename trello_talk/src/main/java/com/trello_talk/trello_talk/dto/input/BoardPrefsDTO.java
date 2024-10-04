package com.trello_talk.trello_talk.dto.input;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardPrefsDTO {
    private String permissionLevel;
    private Boolean hideVotes;
    private String voting;
    private String comments;
    private Boolean selfJoin;
    private Boolean cardCovers;
    private Boolean isTemplate;
    private String cardAging;
    private List<BoardSwitcherViewDTO> switcherViews;
    private String background;
    private String backgroundColor;
    private String backgroundImage;
}