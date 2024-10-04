package com.trello_talk.trello_talk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.trello_talk.trello_talk.dto.input.BoardInputDTO;
import com.trello_talk.trello_talk.dto.output.BoardOutputDTO;
import com.trello_talk.trello_talk.model.Board;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    Board toModel(BoardInputDTO inputDTO);

    @Mapping(target = "limits", ignore = true)
    @Mapping(target = "prefs", ignore = true)
    @Mapping(target = "labelNames", ignore = true)
    @Mapping(target = "powerUps", ignore = true)
    @Mapping(target = "memberships", ignore = true)
    @Mapping(target = "premiumFeatures", ignore = true)
    BoardInputDTO toInputDto(Board board);

    BoardOutputDTO toOutputDto(BoardInputDTO board);
}
