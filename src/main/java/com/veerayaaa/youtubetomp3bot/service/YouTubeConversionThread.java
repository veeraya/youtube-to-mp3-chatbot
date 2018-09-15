package com.veerayaaa.youtubetomp3bot.service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.veerayaaa.youtubetomp3bot.YouTubeToMp3BotApp;
import com.veerayaaa.youtubetomp3bot.model.ConversionWorkUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Scope("prototype")
@Service
public class YouTubeConversionThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(YouTubeConversionThread.class);
    @Autowired
    private YoutubeDownloadService youtubeDownloadService;
    @Autowired
    private LineMessagingClient lineMessagingClient;
    @Autowired
    private TelegramClient telegramClient;

    public static Map<String, String> userMap = new ConcurrentHashMap<>();

    @Override
    public void run() {
        try {
            while (true) {
                ConversionWorkUnit workUnit = YouTubeToMp3BotApp.conversionQueue.take();
                logger.info("Received work unit: [{}]", workUnit);
                try {
                    String downloadLink = youtubeDownloadService.getLinkFromVideo(workUnit.getYoutubeLink());
                    reply(workUnit, downloadLink);

                } catch (Exception e) {
                    logger.error("Error converting or replying: {}", e);
                    logger.info("Replying with error message: {}", e.getMessage());
                    reply(workUnit, e.getMessage());
                    //logUsage(workUnit, PROCESSING_STATUS.FAIL); // send me a LINE msg for each conversion request
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void reply(ConversionWorkUnit workUnit, String message) {
        switch (workUnit.getSource()) {
            case LINE:
                this.lineMessagingClient.pushMessage(new PushMessage(workUnit.getReplyTo(), new TextMessage(message)));
                break;
            case TELEGRAM:
                SendMessage sendMessage = new SendMessage()
                        .setChatId(workUnit.getReplyTo())
                        .setText(message);
                try {
                    telegramClient.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void logUsage(ConversionWorkUnit workUnit, PROCESSING_STATUS processingStatus) {
        try {
            String userId = workUnit.getReplyTo();
            if (!userMap.containsKey(userId)) {
                lineMessagingClient
                        .getProfile(userId)
                        .whenComplete((profile, throwable) -> {
                            userMap.put(userId, profile.getDisplayName());
                        })
                        .join();
            }
            List<Message> messages = new ArrayList<>();
            messages.add(new TextMessage("Received message from " + userMap.getOrDefault(userId, userId) + " : " + workUnit.getYoutubeLink()));
            messages.add(new TextMessage("Status: " + processingStatus));
            this.lineMessagingClient.pushMessage(new PushMessage("U1f9161c159e4273595aef9def40a8618", messages));
        } catch (Exception e) {
            logger.error("error in metric:", e);
        }
    }

    enum PROCESSING_STATUS {
        SUCCESS, FAIL
    }
}
