package com.trello_talk.trello_talk.model;

import java.util.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "board", schema = "public")
public class Board {

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

    @Column(name = "url")
    private String url;

    @Column(name = "date_last_activity")
    private Calendar dateLastActivity;

    @Column(name = "date_last_view")
    private Calendar dateLastView;

    @Column(name = "id_member_creator")
    private String idMemberCreator;

    @Column(name = "id_workspace")
    private String idWorkspace;
}
