package com.trello_talk.trello_talk.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardLimitsDTO {
    private BoardAttachmentsDTO attachments;
    private BoardCardsDTO cards;
    private BoardChecklistsDTO checklists;
    private BoardLabelsDTO labels;
    private BoardListsDTO lists;
    private BoardStickersDTO stickers;
}
