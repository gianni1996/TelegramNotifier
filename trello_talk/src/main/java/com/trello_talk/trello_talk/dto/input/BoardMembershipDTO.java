package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardMembershipDTO {
    private String id;
    private String idMember;
    private String memberType;
    private Boolean unconfirmed;
    private Boolean deactivated;
}
