package com.trello_talk.trello_talk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.trello_talk.trello_talk.dto.input.TrelloCardInputDTO;
import com.trello_talk.trello_talk.dto.output.TrelloCardOutputDTO;
import com.trello_talk.trello_talk.model.TrelloCard;

@Mapper(componentModel = "spring")
public interface TrelloCardMapper {
    TrelloCardMapper INSTANCE = Mappers.getMapper(TrelloCardMapper.class);

    TrelloCard toModel(TrelloCardInputDTO inputDTO);
    TrelloCardInputDTO toInputDto(TrelloCard card);
    TrelloCardOutputDTO toOutputDto(TrelloCardInputDTO card);
}
