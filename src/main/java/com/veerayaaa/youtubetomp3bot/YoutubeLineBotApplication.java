package com.veerayaaa.youtubetomp3bot;

import com.veerayaaa.youtubetomp3bot.model.YoutubeWorkUnit;
import com.veerayaaa.youtubetomp3bot.service.ConversionWorkerThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@SpringBootApplication
public class YoutubeLineBotApplication {
	public static Path downloadedContentDir;
	public static BlockingQueue<YoutubeWorkUnit> youtubeWorkQueue;

	public static void main(String[] args) throws IOException {
		File dir = new File("/tmp/donwloadedVideo");
		dir.mkdir();
		dir.setWritable(true, false);
		dir.setReadable(true, false);
		downloadedContentDir = Paths.get("/tmp/downloadedVideo");
		youtubeWorkQueue = new ArrayBlockingQueue<>(10);

		ConfigurableApplicationContext context = SpringApplication.run(YoutubeLineBotApplication.class, args);

		context.getBean(ConversionWorkerThread.class).run();
	}
}