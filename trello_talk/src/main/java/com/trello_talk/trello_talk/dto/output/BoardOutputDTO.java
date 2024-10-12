package com.trello_talk.trello_talk.dto.output;

import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardOutputDTO {
    private String id;
    private String nodeId;
    private String name;
    private String description;
    private Boolean closed;
    private String idOrganization;
    private String url;
    private Calendar dateLastActivity;
    private Calendar dateLastView;
    private String idMemberCreator;
    private String idWorkspace;
}
