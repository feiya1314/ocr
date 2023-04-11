package cn.easyocr.ai.chat.service.config;

import cn.easyocr.common.redis.JedisManager;
import cn.easyocr.common.redis.RedisConfig;
import cn.easyocr.common.redis.RedissonManager;
import cn.easyocr.common.thread.RequestLogThreadPool;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : feiya
 * @description :
 * @since : 2023/4/9
 */
@Configuration
public class BeansConfig {
    @Bean
    public RequestLogThreadPool requestLogThreadPool() {
        return new RequestLogThreadPool();
    }

    @Bean
    public JedisManager jedisManager() {
        JedisManager jedisManager = new JedisManager(redisConfig());
        jedisManager.init();
        return jedisManager;
    }

    @Bean
    public RedissonManager redissonManager() {
        RedissonManager redissonManager = new RedissonManager(redisConfig());
        redissonManager.init();
        return redissonManager;
    }

    @ConfigurationProperties(prefix = "redis")
    @Bean
    public RedisConfig redisConfig() {
        return new RedisConfig();
    }
}
