package com.telegram_notifier.telegram_notifier.mapper;

import org.mapstruct.Mapper;

import com.telegram_notifier.telegram_notifier.dto.input.TrelloListInputDTO;
import com.telegram_notifier.telegram_notifier.model.TrelloList;

@Mapper(componentModel = "spring")
public interface TrelloListMapper {
    TrelloList toModel(TrelloListInputDTO inputDTO);
    TrelloListInputDTO toDto(TrelloList list);
}
