package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembershipDTO{
    public String id;
    public String idMember;
    public String memberType;
    public boolean unconfirmed;
    public boolean deactivated;
}
