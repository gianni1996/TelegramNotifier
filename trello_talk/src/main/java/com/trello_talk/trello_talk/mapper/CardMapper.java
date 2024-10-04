package com.trello_talk.trello_talk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.trello_talk.trello_talk.dto.input.CardInputDTO;
import com.trello_talk.trello_talk.dto.output.CardOutputDTO;
import com.trello_talk.trello_talk.model.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    Card toModel(CardInputDTO inputDTO);
    CardInputDTO toInputDto(Card card);
    CardOutputDTO toOutputDto(CardInputDTO card);
}
