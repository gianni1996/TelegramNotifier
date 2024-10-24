package com.trello_talk.trello_talk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.trello_talk.trello_talk.model.TrelloList;

@Repository
public interface TrelloListRepository extends JpaRepository<TrelloList, String> {
     List<TrelloList> findByIdBoard(String boardId);
}