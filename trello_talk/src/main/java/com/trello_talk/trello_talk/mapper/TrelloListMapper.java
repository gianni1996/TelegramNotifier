package com.trello_talk.trello_talk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.trello_talk.trello_talk.dto.input.TrelloListInputDTO;
import com.trello_talk.trello_talk.dto.output.TrelloListOutputDTO;
import com.trello_talk.trello_talk.model.TrelloList;

@Mapper(componentModel = "spring")
public interface TrelloListMapper {
    TrelloListMapper INSTANCE = Mappers.getMapper(TrelloListMapper.class);
    TrelloList toModel(TrelloListInputDTO inputDTO);
    TrelloListInputDTO toInputDto(TrelloList list);
    TrelloListOutputDTO toOutputDto(TrelloListInputDTO list);
}
