package com.trello_talk.trello_talk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.trello_talk.trello_talk.dto.input.BoardInputDTO;
import com.trello_talk.trello_talk.dto.output.BoardOutputDTO;
import com.trello_talk.trello_talk.model.Board;
import com.trello_talk.trello_talk.model.BoardBackup;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    @Mapping(target = "descData", ignore = true)
    @Mapping(target = "limits", ignore = true)
    @Mapping(target = "prefs", ignore = true)
    @Mapping(target = "labelNames", ignore = true)
    @Mapping(target = "powerUps", ignore = true)
    @Mapping(target = "memberships", ignore = true)
    @Mapping(target = "premiumFeatures", ignore = true)
    @Mapping(target = "creationMethod", ignore = true)
    @Mapping(target = "dateClosed", ignore = true)
    @Mapping(target = "datePluginDisable", ignore = true)
    @Mapping(target = "enterpriseOwned", ignore = true)
    @Mapping(target = "idBoardSource", ignore = true)
    @Mapping(target = "idEnterprise", ignore = true)
    @Mapping(target = "idTags", ignore = true)
    @Mapping(target = "ixUpdate", ignore = true)
    @Mapping(target = "pinned", ignore = true)
    @Mapping(target = "shortLink", ignore = true)
    @Mapping(target = "shortUrl", ignore = true)
    @Mapping(target = "subscribed", ignore = true)
    @Mapping(target = "templateGallery", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "starred", ignore = true)
    @Mapping(target ="desc", source="description")
    BoardInputDTO toInputDto(Board board);
    
    @Mapping(target = "idWorkspace", ignore = true)
    @Mapping(target ="description", source="desc")
    Board toModel(BoardInputDTO inputDTO);
    
    @Mapping(target = "idWorkspace", ignore = true)
    Board toModel(BoardOutputDTO boardOutputDTO);
    
    @Mapping(target = "idWorkspace", ignore = true)
    @Mapping(target ="description", source="desc")
    BoardOutputDTO toOutputDto(BoardInputDTO board);

    @Mapping(target ="idWorkspaceBackup", source="idWorkspace")
    BoardBackup toBackup(Board board);
}
