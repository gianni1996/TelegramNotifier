package com.trello_talk.trello_talk.model;

import java.util.Calendar;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "card_backup", schema = "public")
public class CardBackup {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "id_list_backup")
    private String idListBackup;

    @Column(name = "id_board")
    private String idBoard;

    @Column(name = "due")
    private Calendar due;

    @Column(name = "closed")
    private Boolean closed;

    @Column(name = "position")
    private Integer position;

    @Column(name = "labels")
    private String labels;

    @Column(name = "url")
    private String url;
}
