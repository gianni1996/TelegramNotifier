package com.trello_talk.trello_talk.dto.input;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardInputDTO {
    private String id;
    private String nodeId;
    private String name;
    private String desc;
    private Boolean closed;
    private String idOrganization;
    private String url;
    private BoardLimitsDTO limits;
    private BoardPrefsDTO prefs;
    private String shortLink;
    private Boolean subscribed;
    private BoardLabelNamesDTO labelNames;
    private List<String> powerUps;
    private String dateLastActivity;
    private String dateLastView;
    private List<BoardMembershipDTO> memberships;
    private String idMemberCreator;
    private List<String> premiumFeatures;
}
