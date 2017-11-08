package com.pook.youtubelinebot;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by pook on 8/11/2017.
 */
@Component
public class ConversionWorkerThread implements Runnable  {
    private static final Logger logger = LoggerFactory.getLogger(ConversionWorkerThread.class);
    @Autowired
    private YoutubeDownloadService youtubeDownloadService;
    @Autowired
    private LineMessagingClient lineMessagingClient;
    @Override
    public void run() {
        try {
            while (true) {
                YoutubeWorkUnit workUnit = YoutubeLineBotApplication.youtubeWorkQueue.take();
                logger.info("Received work unit: [{}]", workUnit);
                try {
                    String downloadLink = youtubeDownloadService.getLinkFromVideo(workUnit.getText());
                    this.lineMessagingClient.pushMessage(new PushMessage(workUnit.getUser(), new TextMessage(downloadLink)));
                } catch (Exception e) {
                    this.lineMessagingClient.pushMessage(new PushMessage(workUnit.getUser(), new TextMessage(e.getMessage())));
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
