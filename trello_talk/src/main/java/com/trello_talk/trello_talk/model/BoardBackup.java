package com.trello_talk.trello_talk.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "board_backup")
public class BoardBackup {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "node_id")
    private String nodeId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "closed")
    private Boolean closed;

    @Column(name = "id_organization")
    private String idOrganization;

    @Column(name = "url")
    private String url;

    @Column(name = "date_last_activity")
    private Calendar dateLastActivity;

    @Column(name = "date_last_view")
    private Calendar dateLastView;

    @Column(name = "id_member_creator")
    private String idMemberCreator;

    @Column(name= "id_workspace")
    private String idWorkspace;
}
