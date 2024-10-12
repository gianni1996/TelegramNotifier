package com.trello_talk.trello_talk.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.trello_talk.trello_talk.model.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
}
