package com.trello_talk.trello_talk.model;

import java.util.Calendar;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "operation", schema = "public")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operation_id_seq")
    @SequenceGenerator(name = "operation_id_seq", sequenceName = "operation_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "work_item", nullable = false)
    private String workItem;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "outcome", nullable = false)
    private Boolean outcome;

    @Column(name = "error_message", nullable = true)
    private String errorMessage;

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(name = "timestamp", nullable = false)
    private Calendar timestamp;
}
