package com.trello_talk.trello_talk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    private String id;
    private String nodeId;
    private String name;
    private String desc;
    private Calendar dateClosed;
    private Boolean closed;
    private String idOrganization;
    private String url;
    private String shortLink;
    private Boolean subscribed;
    private Calendar dateLastActivity;
    private Calendar dateLastView;
    private String idMemberCreator;
}
