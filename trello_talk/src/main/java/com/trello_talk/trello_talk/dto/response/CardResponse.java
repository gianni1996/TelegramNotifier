package com.trello_talk.trello_talk.dto.response;

import com.trello_talk.trello_talk.dto.output.CardOutputDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponse {
    private CardOutputDTO card;
}