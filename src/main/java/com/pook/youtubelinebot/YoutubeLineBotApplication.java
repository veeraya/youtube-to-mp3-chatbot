package com.pook.youtubelinebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@SpringBootApplication
public class YoutubeLineBotApplication {
	static Path downloadedContentDir;
	static BlockingQueue<YoutubeWorkUnit> youtubeWorkQueue;
	public static void main(String[] args) throws IOException {
		Set<PosixFilePermission> perms = new HashSet<>();
		perms.add(PosixFilePermission.OWNER_WRITE);
		perms.add(PosixFilePermission.OWNER_READ);
		perms.add(PosixFilePermission.GROUP_READ);
		perms.add(PosixFilePermission.GROUP_WRITE);
		perms.add(PosixFilePermission.OTHERS_READ);
		perms.add(PosixFilePermission.OTHERS_WRITE);
		FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(perms);

		downloadedContentDir = Files.createTempDirectory("downloadedVideo", fileAttributes);
		youtubeWorkQueue = new ArrayBlockingQueue<>(10);

		ConfigurableApplicationContext context = SpringApplication.run(YoutubeLineBotApplication.class, args);

		context.getBean(ConversionWorkerThread.class).run();
	}
}
