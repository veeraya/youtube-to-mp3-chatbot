package com.pook.youtubelinebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class YoutubeLineBotApplication {
	static Path downloadedContentDir;
	static Path uploadedContentDir;
	public static void main(String[] args) throws IOException {
		downloadedContentDir = Files.createTempDirectory("downloadedVideo");
		uploadedContentDir = Files.createTempDirectory("uploadedAudio");
		SpringApplication.run(YoutubeLineBotApplication.class, args);
	}
}
