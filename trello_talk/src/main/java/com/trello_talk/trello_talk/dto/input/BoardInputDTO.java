package com.trello_talk.trello_talk.dto.input;

import java.util.ArrayList;
import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardInputDTO {
    public String id;
    public String nodeId;
    public String name;
    public String desc;
    public DescDataDTO descData;
    public Boolean closed;
    public Calendar dateClosed;
    public String idOrganization;
    public Object idEnterprise;
    public LimitsDTO limits;
    public Boolean pinned;
    public Boolean starred;
    public String url;
    public PrefsDTO prefs;
    public String shortLink;
    public Boolean subscribed;
    public LabelNamesDTO labelNames;
    public ArrayList<Object> powerUps;
    public Calendar dateLastActivity;
    public Calendar dateLastView;
    public String shortUrl;
    public ArrayList<Object> idTags;
    public Calendar datePluginDisable;
    public String creationMethod;
    public String ixUpdate;
    public Object templateGallery;
    public Boolean enterpriseOwned;
    public Object idBoardSource;
    public ArrayList<String> premiumFeatures;
    public String idMemberCreator;
    public Object type;
    public ArrayList<MembershipDTO> memberships;
}
