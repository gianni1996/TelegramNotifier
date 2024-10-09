package com.trello_talk.trello_talk.dto.input;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrefsDTO{
    public String permissionLevel;
    public Boolean hideVotes;
    public String voting;
    public String comments;
    public String invitations;
    public Boolean selfJoin;
    public Boolean cardCovers;
    public Boolean cardCounts;
    public Boolean isTemplate;
    public String cardAging;
    public Boolean calendarFeedEnabled;
    public ArrayList<Object> hiddenPluginBoardButtons;
    public ArrayList<SwitcherViewDTO> switcherViews;
    public String background;
    public String backgroundColor;
    public String backgroundImage;
    public Boolean backgroundTile;
    public String backgroundBrightness;
    public Object sharedSourceUrl;
    public Object backgroundImageScaled;
    public String backgroundBottomColor;
    public String backgroundTopColor;
    public Boolean canBePublic;
    public Boolean canBeEnterprise;
    public Boolean canBeOrg;
    public Boolean canBePrivate;
    public Boolean canInvite;
}
