package com.pook.youtubelinebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@SpringBootApplication
public class YoutubeLineBotApplication {
	static Path downloadedContentDir;
	static BlockingQueue<YoutubeWorkUnit> youtubeWorkQueue;
	public static void main(String[] args) throws IOException {
		downloadedContentDir = Files.createTempDirectory("downloadedVideo");
		youtubeWorkQueue = new ArrayBlockingQueue<>(10);

		ConfigurableApplicationContext context = SpringApplication.run(YoutubeLineBotApplication.class, args);

		context.getBean(ConversionWorkerThread.class).run();
	}
}
