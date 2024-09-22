package com.telegram_notifier.telegram_notifier.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.telegram_notifier.telegram_notifier.dto.input.TrelloCardInputDTO;
import com.telegram_notifier.telegram_notifier.model.TrelloCard;

@Mapper(componentModel = "spring")
public interface TrelloCardMapper {
    TrelloCardMapper INSTANCE = Mappers.getMapper(TrelloCardMapper.class);

    TrelloCard toModel(TrelloCardInputDTO inputDTO);
    TrelloCardInputDTO toDto(TrelloCard card);
}
