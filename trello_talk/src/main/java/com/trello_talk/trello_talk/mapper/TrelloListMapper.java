package com.trello_talk.trello_talk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.trello_talk.trello_talk.dto.input.TrelloListInputDTO;
import com.trello_talk.trello_talk.dto.output.TrelloListOutputDTO;
import com.trello_talk.trello_talk.model.TrelloList;
import com.trello_talk.trello_talk.model.TrelloListBackup;

@Mapper(componentModel = "spring")
public interface TrelloListMapper {
    TrelloListMapper INSTANCE = Mappers.getMapper(TrelloListMapper.class);

    @Mapping(target ="position", source="pos")
    TrelloList toModel(TrelloListInputDTO inputDTO);

    TrelloList toModel(TrelloListOutputDTO outputDTO);

    @Mapping(target ="pos", source="position")
    @Mapping(target = "datasource", ignore = true)
    TrelloListInputDTO toInputDto(TrelloList list);

    @Mapping(target ="position", source="pos")
    TrelloListOutputDTO toOutputDto(TrelloListInputDTO list);

    TrelloListOutputDTO toOutputDto(TrelloList list);

    @Mapping(target ="idBoardBackup", source="idBoard")
    TrelloListBackup toBackup(TrelloList list);
}
