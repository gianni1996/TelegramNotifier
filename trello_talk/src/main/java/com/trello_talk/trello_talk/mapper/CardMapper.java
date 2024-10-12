package com.trello_talk.trello_talk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.trello_talk.trello_talk.dto.input.CardInputDTO;
import com.trello_talk.trello_talk.dto.output.CardOutputDTO;
import com.trello_talk.trello_talk.model.Card;
import com.trello_talk.trello_talk.model.CardBackup;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    @Mapping(target = "attachments", ignore = true)
    @Mapping(target ="desc", source="description")
    @Mapping(target ="pos", source="position")
    CardInputDTO toInputDto(Card card);

    @Mapping(target ="description", source="desc")
    @Mapping(target ="position", source="pos")
    Card toModel(CardInputDTO inputDTO);
    
    CardOutputDTO toOutputDto(CardInputDTO card);

    @Mapping(target ="idListBackup", source="idList")
    CardBackup toBackup(Card card);
}
