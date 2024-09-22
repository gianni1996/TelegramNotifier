package com.telegram_notifier.telegram_notifier.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.telegram_notifier.telegram_notifier.service.MiddleService;

@Component
public class SchedulerTelegramElements {

    @Autowired
    private MiddleService middleService;

    @Scheduled(cron = "#{@telegramConfig.downloadScheduler}")
    public void checkLists() {
        middleService.downloadLists();
    }
}

