package com.trello_talk.trello_talk.model;

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
@Table(name = "list")
public class TrelloList {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "closed")
    private Boolean closed;

    @Column(name = "id_board")
    private String idBoard;

    @Column(name = "position")
    private Integer position;

    @Column(name = "subscribed")
    private Boolean subscribed;

    @Column(name = "color")
    private String color;

    @Column(name = "soft_limit")
    private String softLimit;

    @Column(name = "type")
    private String type;
}
