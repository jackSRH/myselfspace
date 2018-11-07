package com.mailian.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/7/21
 * @Description:
 */
@Configuration
@EnableCaching
public class RedisConfig {
    @Autowired
    private JedisConfig jedisConfig;


    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        RedisStandaloneConfiguration rf=new RedisStandaloneConfiguration();
        rf.setDatabase(jedisConfig.database);
        rf.setHostName(jedisConfig.host);
        rf.setPort(jedisConfig.port);

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpb=
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(jedisConfig.maxIdle);
        jedisPoolConfig.setMinIdle(jedisConfig.minIdle);
        jedisPoolConfig.setMaxTotal(jedisConfig.maxActive);
        jedisPoolConfig.setMaxWaitMillis(jedisConfig.maxWaitMillis);
        jpb.poolConfig(jedisPoolConfig);
        JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory(rf,jpb.build());
        return jedisConnectionFactory;
    }

    @Bean
    public KeyGenerator keyGenerator(){
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };

    }

    /*@Bean
    public CacheManager cacheManager(JedisConnectionFactory jedisConnectionFactory){
        //初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(jedisConnectionFactory);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        //设置默认超过期时间是30秒
        defaultCacheConfig.entryTtl(Duration.ofSeconds(redisExpire));
        //初始化RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
        return cacheManager;
    }*/


    @Bean
    public CacheManager cacheManager(JedisConnectionFactory jedisConnectionFactory) {
        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(jedisConnectionFactory),
                this.getRedisCacheConfigurationWithTtl(jedisConfig.redisExpire), // 默认策略，未配置的 key 会使用这个
                this.getRedisCacheConfigurationMap() // 指定 key 策略
        );
    }

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        /*redisCacheConfigurationMap.put("UserInfoList", this.getRedisCacheConfigurationWithTtl(3000));
        redisCacheConfigurationMap.put("UserInfoListAnother", this.getRedisCacheConfigurationWithTtl(18000));*/

        return redisCacheConfigurationMap;
    }

    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));

        redisCacheConfiguration.entryTtl(Duration.ofSeconds(seconds));
        return redisCacheConfiguration;
    }

    @Bean
    public RedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(jedisConnectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public HashOperations hashOperations(RedisTemplate redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    public ValueOperations<String,String> valueOperations(RedisTemplate redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public ListOperations listOperations(RedisTemplate redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean
    public SetOperations setOperations(RedisTemplate redisTemplate) {
        return redisTemplate.opsForSet();
    }

    @Bean
    public ZSetOperations zSetOperations(RedisTemplate redisTemplate) {
        return redisTemplate.opsForZSet();
    }
}
