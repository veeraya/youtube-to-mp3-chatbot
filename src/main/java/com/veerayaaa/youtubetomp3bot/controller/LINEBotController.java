/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.veerayaaa.youtubetomp3bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.veerayaaa.youtubetomp3bot.YoutubeLineBotApplication;
import com.veerayaaa.youtubetomp3bot.model.YoutubeWorkUnit;
import com.veerayaaa.youtubetomp3bot.util.YoutubeUrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@LineMessageHandler
public class LINEBotController {

    private static final Logger log = LoggerFactory.getLogger(LINEBotController.class);
    @Autowired
    private LineMessagingClient lineMessagingClient;

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        handleSticker(event.getReplyToken(), event.getMessage());
    }

    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        log.info("Received unfollow event: {}", event);
    }

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        log.info("Received follow event: {}", event);
    }

    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
        log.info("Received Join event: {}", event);
    }

    @EventMapping
    public void handleOtherEvent(Event event) {
        log.info("Received message(Ignored): {}", event);
    }

    private void reply(String replyToken, Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(String replyToken, List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .get();
            log.info("Sent messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(String replyToken, String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "……";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    private void handleSticker(String replyToken, StickerMessageContent content) {
        reply(replyToken, new StickerMessage(
                content.getPackageId(), content.getStickerId())
        );
    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content) {
        String text = content.getText();

        log.info("Received text message with token {} from {}: {}", replyToken, event.getSource().getSenderId(), text);
        if (YoutubeUrlUtil.containsYoutubeUrl(text)) {
            this.replyText(replyToken, "Converting... Please wait...");
            try {
                YoutubeLineBotApplication.youtubeWorkQueue.put(new YoutubeWorkUnit(event.getSource().getUserId(), text, YoutubeWorkUnit.Source.LINE));
            } catch (Exception e) {
                this.lineMessagingClient.pushMessage(new PushMessage(event.getSource().getSenderId(), new  TextMessage(e.getMessage())));
            }
            return;
        } else {
            log.info("Returns echo message {}: {}", replyToken, text);
            this.replyText(replyToken, text);
        }
    }
}
