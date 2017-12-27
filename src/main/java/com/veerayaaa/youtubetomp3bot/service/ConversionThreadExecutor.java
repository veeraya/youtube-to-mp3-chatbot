package com.veerayaaa.youtubetomp3bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ConversionThreadExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ConversionThreadExecutor.class);

    public ConversionThreadExecutor(@Autowired ApplicationContext applicationContext, @Autowired TaskExecutor executor,  @Value("${thread.conversion.size}") int numThread) {
        logger.info("Starting {} threads", numThread);
        for (int i = 0; i < numThread; i++) {
            executor.execute(applicationContext.getBean(YouTubeConversionThread.class));
        }
    }
}
