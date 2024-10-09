package com.trello_talk.trello_talk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.trello_talk.trello_talk.dto.input.WorkspaceInputDTO;
import com.trello_talk.trello_talk.dto.output.WorkspaceOutputDTO;
import com.trello_talk.trello_talk.model.Workspace;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {
    WorkspaceMapper INSTANCE = Mappers.getMapper(WorkspaceMapper.class);

    
    @Mapping(target = "activeMembershipCount", ignore = true)
    @Mapping(target = "availableLicenseCount", ignore = true)
    @Mapping(target = "billableCollaboratorCount", ignore = true)
    @Mapping(target = "billableMemberCount", ignore = true)
    @Mapping(target = "boardCounts", ignore = true)
    @Mapping(target = "dateLastActivity", ignore = true)
    @Mapping(target = "descData", ignore = true)
    @Mapping(target = "domainName", ignore = true)
    @Mapping(target = "enterpriseJoinRequest", ignore = true)
    @Mapping(target = "idBoards", ignore = true)
    @Mapping(target = "idEnterprise", ignore = true)
    @Mapping(target = "idEntitlement", ignore = true)
    @Mapping(target = "idMemberCreator", ignore = true)
    @Mapping(target = "invitations", ignore = true)
    @Mapping(target = "invited", ignore = true)
    @Mapping(target = "ixUpdate", ignore = true)
    @Mapping(target = "jwmLink", ignore = true)
    @Mapping(target = "limits", ignore = true)
    @Mapping(target = "logoHash", ignore = true)
    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "maximumLicenseCount", ignore = true)
    @Mapping(target = "membersCount", ignore = true)
    @Mapping(target = "memberships", ignore = true)
    @Mapping(target = "nodeId", ignore = true)
    @Mapping(target = "offering", ignore = true)
    @Mapping(target = "powerUps", ignore = true)
    @Mapping(target = "prefs", ignore = true)
    @Mapping(target = "premiumFeatures", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "promotions", ignore = true)
    @Mapping(target = "reverseTrialTag", ignore = true)
    @Mapping(target = "standardVariation", ignore = true)
    @Mapping(target = "teamType", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "url", ignore = true)
     @Mapping(target = "website", ignore = true)
    @Mapping(target = "desc", source = "description")
    WorkspaceInputDTO toInputDto(WorkspaceOutputDTO workspaceOutputDTO);

    @Mapping(target = "description", source = "desc")
    Workspace toModel(WorkspaceInputDTO workspaceInputDTO);

    @Mapping(target = "description", source = "desc")
    WorkspaceOutputDTO toOutputDto(WorkspaceInputDTO workspaceInputDTO);

    WorkspaceOutputDTO toOutputDto(Workspace workspace); 

}

