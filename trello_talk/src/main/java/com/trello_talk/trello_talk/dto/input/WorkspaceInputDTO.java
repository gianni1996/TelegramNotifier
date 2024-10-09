package com.trello_talk.trello_talk.dto.input;

import java.util.Calendar;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceInputDTO {
    private String id;
    private String name;
    private String displayName;
    private String desc;
    private DescDataDTO descData;
    private String domainName;
    private List<String> idBoards;
    private String idEnterprise;
    private String idEntitlement;
    private String idMemberCreator;
    private Boolean invited;
    private List<String> invitations;
    private LimitsDTO limits;
    private Integer membersCount;
    private String nodeId;
    private PrefsDTO prefs;
    private List<String> powerUps;
    private String offering;
    private List<String> products;
    private Integer billableMemberCount;
    private Integer billableCollaboratorCount;
    private String url;
    private String website;
    private String logoHash;
    private String logoUrl;
    private List<String> premiumFeatures;
    private List<String> promotions;
    private Object enterpriseJoinRequest;
    private String standardVariation;
    private Integer availableLicenseCount;
    private Integer maximumLicenseCount;
    private String ixUpdate;
    private String teamType;
    private Calendar dateLastActivity;
    private String jwmLink;
    private Integer activeMembershipCount;
    private List<BoardCountDTO> boardCounts;
    private String type;
    private String reverseTrialTag;
    private List<MembershipDTO> memberships;
}
