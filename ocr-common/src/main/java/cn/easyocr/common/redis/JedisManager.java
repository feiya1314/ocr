package cn.easyocr.common.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @author : feiya
 * @description :
 * @since : 2023/2/5
 */
public class JedisManager {
    private final RedisConfig redisConfig;

    private volatile JedisPool jedisPool;

    public JedisManager(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    public void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisConfig.getMaxTotal());
        config.setMaxIdle(redisConfig.getMaxIdle());
        config.setMaxWait(Duration.ofMillis(redisConfig.getMaxWaitMills()));
        config.setTestOnBorrow(redisConfig.getTestOnBorrow());

        jedisPool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort());
    }

    public Jedis getJedis() {
        if (jedisPool == null) {
            throw new NullPointerException("jedisPool is null");
        }

        return jedisPool.getResource();
    }
}
