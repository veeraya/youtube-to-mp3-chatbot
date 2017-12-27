package com.veerayaaa.youtubetomp3bot.config;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadConfig {

    @Value("${thread.conversion.size}")
    private int corePoolSize;

    @Value("${thread.conversion.maxPoolSize}")
    private int maxPoolSize;

    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(this.corePoolSize);
        executor.setMaxPoolSize(this.maxPoolSize);
        executor.setThreadNamePrefix("THREAD_");
        executor.initialize();
        return executor;
    }

}
