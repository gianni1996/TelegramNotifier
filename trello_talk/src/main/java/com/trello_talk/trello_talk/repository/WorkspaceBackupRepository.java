package com.trello_talk.trello_talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trello_talk.trello_talk.model.WorkspaceBackup;

@Repository
public interface WorkspaceBackupRepository extends JpaRepository<WorkspaceBackup, String> {
}