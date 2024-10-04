package com.trello_talk.trello_talk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    private String id;
    private String nodeId;
    private String name;
    private String desc;
    private Boolean closed;
    private String idOrganization;
    private String url;
    private String shortLink;
    private Boolean subscribed;
    private String dateLastActivity;
    private String dateLastView;
    private String idMemberCreator;
}
