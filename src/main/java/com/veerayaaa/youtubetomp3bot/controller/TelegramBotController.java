package com.veerayaaa.youtubetomp3bot.controller;

import com.veerayaaa.youtubetomp3bot.YouTubeToMp3BotApp;
import com.veerayaaa.youtubetomp3bot.model.ConversionWorkUnit;
import com.veerayaaa.youtubetomp3bot.util.YoutubeUrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Controller
@ConfigurationProperties(prefix="telegram.bot")
public class TelegramBotController extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotController.class);

    @Value("${telegram.botUserName}")
    private String botUserName;
    @Value("${telegram.botToken}")
    private String botToken;


    @Override
    public void onUpdateReceived(Update update) {
        logger.info("Received Telegram Msg: {}", update);
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (YoutubeUrlUtil.containsYoutubeUrl(update.getMessage().getText())) {
                SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText("Converting.. Please wait...");
                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    logger.error("Error while calling execute: {}", e);
                }
                try {
                    YouTubeToMp3BotApp.conversionQueue.put(new ConversionWorkUnit(update.getMessage().getChatId().toString(), update.getMessage().getText(), ConversionWorkUnit.Source.TELEGRAM));
                } catch (InterruptedException e) {
                    logger.error("Error while putting to queue: {}", e);
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return this.botUserName;
    }


    @Override
    public String getBotToken() {
        return this.botToken;
    }
}
