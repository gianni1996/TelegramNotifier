package com.trello_talk.trello_talk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.trello_talk.trello_talk.dto.input.ListInputDTO;
import com.trello_talk.trello_talk.dto.output.ListOutputDTO;
import com.trello_talk.trello_talk.model.List;

@Mapper(componentModel = "spring")
public interface ListMapper {
    ListMapper INSTANCE = Mappers.getMapper(ListMapper.class);
    List toModel(ListInputDTO inputDTO);
    ListInputDTO toInputDto(List list);
    ListOutputDTO toOutputDto(ListInputDTO list);
}
