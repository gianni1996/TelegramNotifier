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
    public boolean hideVotes;
    public String voting;
    public String comments;
    public String invitations;
    public boolean selfJoin;
    public boolean cardCovers;
    public boolean cardCounts;
    public boolean isTemplate;
    public String cardAging;
    public boolean calendarFeedEnabled;
    public ArrayList<Object> hiddenPluginBoardButtons;
    public ArrayList<SwitcherViewDTO> switcherViews;
    public String background;
    public String backgroundColor;
    public String backgroundImage;
    public boolean backgroundTile;
    public String backgroundBrightness;
    public Object sharedSourceUrl;
    public Object backgroundImageScaled;
    public String backgroundBottomColor;
    public String backgroundTopColor;
    public boolean canBePublic;
    public boolean canBeEnterprise;
    public boolean canBeOrg;
    public boolean canBePrivate;
    public boolean canInvite;
}
