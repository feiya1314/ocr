package cn.easyocr.common.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author : feiya
 * @description : redisson 管理
 * @since : 2023/2/5
 */
public class RedissonManager {
    private final RedisConfig redisConfig;

    private volatile RedissonClient redissonClient;

    public RedissonManager(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

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
