package com.telegram_notifier.telegram_notifier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.telegram_notifier.telegram_notifier.config.TelegramConfig;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

@Service
public class TelegramService extends TelegramLongPollingBot {

    @Autowired
    private TelegramConfig telegramConfig;

    @Override
    public String getBotToken() {
        return telegramConfig.getBotToken();
    }

    @Override
    public String getBotUsername() {
        return telegramConfig.getBotUsername();
    }

    @Override
    public void onUpdateReceived(org.telegram.telegrambots.meta.api.objects.Update update) {
        // Gestione dei messaggi in arrivo, se necessaria
    }

    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);  // Invia il messaggio
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
