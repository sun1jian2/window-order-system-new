package com.window.system.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Slf4j
@Configuration
public class ThreadPoolConfig {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, Executor> threadPools;

    /**
     * spring 容器是否关闭
     */
    public static volatile boolean isShuttingDown = false;

    @PostConstruct
    public void init() {
        threadPools = applicationContext.getBeansOfType(Executor.class);
        log.warn("thread pool size = {}", threadPools.size());
        threadPools.forEach((k, v) -> {
            log.warn("{} = {}", k, v);
        });
    }

    @PreDestroy
    public void preDestroy() {
        isShuttingDown = true;
        threadPools.forEach((k, v) -> {
            if (v instanceof ThreadPoolExecutor) {
                ((ThreadPoolExecutor) v).shutdown();
            }
        });
    }

    @Bean(name = "taskExportExecutor")
    public Executor taskExportExecutor() {
        String uuid = UUID.randomUUID().toString();
        int processorCount = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(processorCount, processorCount,
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(16),
                new CustomizableThreadFactory("async-task-export-" + uuid + "-"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

}