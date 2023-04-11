package cn.easy.ocr.main.service.redis;

import cn.easy.ocr.main.service.BaseTest;
import cn.easyocr.common.redis.JedisManager;
import cn.easyocr.common.redis.RedissonManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

/**
 * @author : feiya
 * @description :
 * @since : 2023/2/5
 */
public class RedisTest extends BaseTest {
    @Autowired
    private RedissonManager redissonManager;

    @Autowired
    private JedisManager jedisManager;

    @Test
    public void testSet() {
        try (Jedis jedis = jedisManager.getJedis()) {
            String ret = jedis.set("test", "124");
            Assertions.assertEquals("OK", ret, "redis set failed");
        }
    }

    @Test
    public void testGet() {
        try (Jedis jedis = jedisManager.getJedis()) {
            String ret = jedis.get("test");
            Assertions.assertEquals("124", ret, "redis get failed");
        }
    }
}
