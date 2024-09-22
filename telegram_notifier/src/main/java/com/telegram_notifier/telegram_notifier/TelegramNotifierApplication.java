package com.telegram_notifier.telegram_notifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAutoConfiguration
@EnableConfigurationProperties
@SpringBootApplication
public class TelegramNotifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramNotifierApplication.class, args);
    }
}

