package cn.easy.ocr.main.service.redis;

import cn.easy.ocr.main.service.config.RedisConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author : feiya
 * @description : redisson 管理
 * @since : 2023/2/5
 */
@Component
public class RedissonManager {
    private final RedisConfig redisConfig;

    private volatile RedissonClient redissonClient;

    @Autowired
    public RedissonManager(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    @PostConstruct
    public void init() {
        if (redisConfig == null) {
            throw new NullPointerException("redissonConfig is null");
        }

        if (redissonClient != null) {
            return;
        }

        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redisConfig.getHost() + ":" + redisConfig.getPort());

        redissonClient = Redisson.create(config);
    }

    public RedissonClient getRedissonClient() {
        return this.redissonClient;
    }
}
