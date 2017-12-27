package com.veerayaaa.youtubetomp3bot.controller;

import com.veerayaaa.youtubetomp3bot.service.YoutubeDownloadService;
import com.veerayaaa.youtubetomp3bot.YoutubeLineBotApplication;
import com.veerayaaa.youtubetomp3bot.model.YoutubeWorkUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.io.IOException;

/**
 * Created by pook on 8/11/2017.
 */
@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    YoutubeDownloadService youtubeDownloadService;

    @GetMapping("/test")
    public String test(@RequestParam("url") String url) throws IOException, InterruptedException {
        YoutubeLineBotApplication.youtubeWorkQueue.put(new YoutubeWorkUnit("some user id", url));
        return "";
    }

    @GetMapping("/hello")
    public String hello(@PathParam("link") String link) throws IOException {
        return "helloworldd";
    }
}
