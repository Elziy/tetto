package com.elite.tetto.image.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadConfig {
    private static final Integer corePoolSize = 20;
    private static final Integer maxSize = 200;
    private static final Integer keepAliveTime = 10;
    public static final int BLOCKING_QUEUE_CAPACITY = 100000;
    
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maxSize,
                keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(BLOCKING_QUEUE_CAPACITY),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        return threadPoolExecutor;
    }
}
