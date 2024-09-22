package com.telegram_notifier.telegram_notifier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.telegram_notifier.telegram_notifier.config.TelegramConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode(ParseMode.MARKDOWN)
                .build();

        try {
            execute(message); // Invia il messaggio
        } catch (TelegramApiException e) {
            log.error("Errore durante l'invio del messaggio", e);
        }
    }

    public void sendPhoto(String chatId, InputFile photo) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(photo);

        try {
            execute(sendPhoto); // Invia il grafico
        } catch (TelegramApiException e) {
            log.error("Errore durante l'invio dell' immagine", e);
        }
    }

}
