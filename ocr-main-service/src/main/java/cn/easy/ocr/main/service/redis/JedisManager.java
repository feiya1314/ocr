package cn.easy.ocr.main.service.redis;

import cn.easy.ocr.main.service.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.time.Duration;

/**
 * @author : feiya
 * @description :
 * @since : 2023/2/5
 */
@Component
public class JedisManager {
    private final RedisConfig redisConfig;

    private volatile JedisPool jedisPool;

    @Autowired
    public JedisManager(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    @PostConstruct
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
