package com.trello_talk.trello_talk.model;

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
@Table(name = "list_backup", schema = "public")
public class TrelloListBackup {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "closed")
    private Boolean closed;

    @Column(name = "id_board_backup")
    private String idBoardBackup;

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
