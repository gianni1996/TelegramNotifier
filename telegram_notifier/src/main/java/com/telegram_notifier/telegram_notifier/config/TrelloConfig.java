package com.telegram_notifier.telegram_notifier.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "trello")
public class TrelloConfig {

    private String apiKey;
    private String oauthToken;
    private String boardId;
}
