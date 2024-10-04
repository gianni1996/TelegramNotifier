package com.trello_talk.trello_talk.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardOutputDTO {
    private String id;
    private String name;
}
