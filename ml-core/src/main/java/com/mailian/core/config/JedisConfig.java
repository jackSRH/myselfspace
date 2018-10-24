package com.mailian.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/8/2
 * @Description:
 */
@Component
@Configuration
public class JedisConfig {
    @Value("${spring.redis.host}")
    public String host;
    @Value("${spring.redis.port}")
    public int port;
    @Value("${spring.redis.database}")
    public int database;
    @Value("${spring.redis.jedis.pool.maxIdle}")
    public int maxIdle;
    @Value("${spring.redis.jedis.pool.minIdle}")
    public int minIdle;
    @Value("${spring.redis.jedis.pool.maxActive}")
    public int maxActive;
    @Value("${spring.redis.jedis.pool.maxWaitMillis}")
    public int maxWaitMillis;
    @Value("${spring.redis.timeout}")
    public String timeout;
    @Value("${spring.redis.expire}")
    public Integer redisExpire;
}
