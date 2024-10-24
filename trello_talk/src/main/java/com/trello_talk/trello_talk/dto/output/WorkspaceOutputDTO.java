package com.trello_talk.trello_talk.dto.output;

import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceOutputDTO {

    private String id;
    private String name;
    private String displayName;
    private String description;
    private String domainName;
    private String idEnterprise;
    private String idEntitlement;
    private String idMemberCreator;
    private Boolean invited;
    private Integer membersCount;
    private String nodeId;
    private String offering;
    private Integer billableMemberCount;
    private Integer billableCollaboratorCount;
    private String url;
    private String website;
    private String logoUrl;
    private String standardVariation;
    private Integer availableLicenseCount;
    private Integer maximumLicenseCount;
    private String ixUpdate;
    private String teamType;
    private Calendar dateLastActivity;
    private String jwmLink;
    private Integer activeMembershipCount;
    private String type;
    private String reverseTrialTag;
}
