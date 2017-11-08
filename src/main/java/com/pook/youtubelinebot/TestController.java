package com.pook.youtubelinebot;

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
    public String test(@RequestParam("url") String url) throws IOException {
        String downloadLink = youtubeDownloadService.getMp3LinkFromVideo(url);
        return downloadLink;
    }

    @GetMapping("/hello")
    public String hello(@PathParam("link") String link) throws IOException {
        return "helloworld";
    }
}
