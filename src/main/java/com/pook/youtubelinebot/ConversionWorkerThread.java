package com.pook.youtubelinebot;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pook on 8/11/2017.
 */
@Component
public class ConversionWorkerThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ConversionWorkerThread.class);
    @Autowired
    private YoutubeDownloadService youtubeDownloadService;
    @Autowired
    private LineMessagingClient lineMessagingClient;

    public static Map<String, String> userMap = new ConcurrentHashMap<>();

    @Override
    public void run() {
        try {
            while (true) {
                YoutubeWorkUnit workUnit = YoutubeLineBotApplication.youtubeWorkQueue.take();
                logger.info("Received work unit: [{}]", workUnit);
                try {
                    String downloadLink = youtubeDownloadService.getLinkFromVideo(workUnit.getText());
                    this.lineMessagingClient.pushMessage(new PushMessage(workUnit.getUser(), new TextMessage(downloadLink)));
                    logMetric(workUnit, PROCESSING_STATUS.SUCCESS);
                } catch (Exception e) {
                    this.lineMessagingClient.pushMessage(new PushMessage(workUnit.getUser(), new TextMessage(e.getMessage())));
                    logMetric(workUnit, PROCESSING_STATUS.FAIL);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void logMetric(YoutubeWorkUnit workUnit, PROCESSING_STATUS processingStatus) {
        try {
            String userId = workUnit.getUser();
            if (!userMap.containsKey(userId)) {
                lineMessagingClient
                        .getProfile(userId)
                        .whenComplete((profile, throwable) -> {
                            userMap.put(userId, profile.getDisplayName());
                        })
                        .join();
            }
            List<Message> messages = new ArrayList<>();
            messages.add(new TextMessage("Received message from " + userMap.getOrDefault(userId, userId) + " : " + workUnit.getText()));
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
