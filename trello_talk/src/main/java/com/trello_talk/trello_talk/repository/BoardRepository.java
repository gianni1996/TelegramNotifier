package com.trello_talk.trello_talk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trello_talk.trello_talk.model.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {
    List<Board> findByIdWorkspace(String idWorkspace);
}