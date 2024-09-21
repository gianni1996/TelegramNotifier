package com.telegram_notifier.telegram_notifier.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegram")
public class TelegramConfig {
    
    private String chatid;
    private String botUsername;
    private String botToken; 
    private String downloadScheduler;
}

