package com.trello_talk.trello_talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.trello_talk.trello_talk.model.BoardBackup;

@Repository
public interface BoardBackupRepository extends JpaRepository<BoardBackup, String> {
}