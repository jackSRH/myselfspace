package com.mailian.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/23
 * @Description:线程池
 */
@Configuration
public class ThreadPoolConfig {
    @Bean("executorService")
    public ExecutorService getThreadPool(){
        return Executors.newFixedThreadPool(100);
    }
}
