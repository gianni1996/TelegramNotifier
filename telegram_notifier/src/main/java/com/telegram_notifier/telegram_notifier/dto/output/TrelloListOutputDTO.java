package com.telegram_notifier.telegram_notifier.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrelloListOutputDTO {
    private String id;
    private String name;
}
