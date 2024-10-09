package com.trello_talk.trello_talk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAutoConfiguration
@EnableConfigurationProperties
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.trello_talk.trello_talk.model")
public class TrelloTalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrelloTalkApplication.class, args);
    }
}

