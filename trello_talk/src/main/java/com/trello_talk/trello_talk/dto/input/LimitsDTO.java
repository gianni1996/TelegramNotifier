package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitsDTO{
    public AttachmentsDTO attachments;
    public BoardDetailsDTO boards;
    public CardDetailsDTO cards;
    public ChecklistsDTO checklists;
    public CheckItemsDTO checkItems;
    public CustomFieldsDTO customFields;
    public CustomFieldOptionsDTO customFieldOptions;
    public LabelsDTO labels;
    public ListsDTO lists;
    public StickersDTO stickers;
    public ReactionsDTO reactions;
}
