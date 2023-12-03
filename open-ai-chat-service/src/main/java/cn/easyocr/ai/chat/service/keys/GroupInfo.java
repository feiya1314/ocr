package cn.easyocr.ai.chat.service.keys;

import cn.easyocr.ai.chat.service.enums.ChatSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : feiya
 * @date : 2023/10/21
 * @description : 每个chat source的api key对应一个group ，例如chat gpt 对应一个group 可以有多个 key，api2d 也有一个自己的group，也可有多个key
 */
public class GroupInfo {
    /**
     * 每个source 对应一种chat的实现方式
     */
    public ChatSource chatSource;
    public int priority;

    public List<KeyInfo> availableKeys;

    private final List<KeyInfo> invalidKeys = new ArrayList<>();

    private final Map<KeyInfo, InvalidCounter> validCounters = new ConcurrentHashMap<>();

    private final Random random = new Random();

    private final byte[] invalidLock = new byte[0];
    private final static long INVALID_WINDOW = 20 * 1000;
    private final static long MAX_COUNT = 3;

    public KeyInfo randomKeyInfo() {
        synchronized (invalidLock) {
            return availableKeys.get(random.nextInt(availableKeys.size()));
        }
    }

    public void invalidKey(KeyInfo keyInfo) {
        InvalidCounter existCounter = validCounters.computeIfPresent(keyInfo, (k, counter) -> {
            doInvalidKey(keyInfo, counter);
            return counter;
        });

        if (existCounter != null) {
            return;
        }

        // 该key已经加入到invalidKeys了，直接返回
        if (keyInList(invalidKeys, keyInfo)) {
            return;
        }

        // 该key不在invalidKeys和availableKeys，直接返回
        if (!keyInList(availableKeys, keyInfo)) {
            return;
        }

        // 该key在availableKeys
        validCounters.compute(keyInfo, (k, counter) -> {
            if (counter == null) {
                return new InvalidCounter(new AtomicInteger(1), System.currentTimeMillis());
            }

            doInvalidKey(keyInfo, counter);
            return counter;
        });
    }

    private void doInvalidKey(KeyInfo keyInfo, InvalidCounter counter) {
        if (System.currentTimeMillis() - counter.lastUpdateTime > INVALID_WINDOW) {
            counter.count.set(1);
            keyInfo.resetKeyAvailable();
            counter.lastUpdateTime = System.currentTimeMillis();
            return;
        }

        counter.count.updateAndGet(prev -> {
            if (prev < MAX_COUNT) {
                counter.lastUpdateTime = System.currentTimeMillis();
                return prev + 1;
            }
            // 移动到invalidKeys
            if (prev >= MAX_COUNT) {
                synchronized (invalidLock) {
                    invalidKeys.add(keyInfo);
                    availableKeys.removeIf(k -> k.key.equals(keyInfo.key));
                }
            }
            return prev;
        });
    }

    private boolean keyInList(List<KeyInfo> keys, KeyInfo keyInfo) {
        if (keys == null || keys.isEmpty()) {
            return false;
        }
        synchronized (invalidLock) {
            for (KeyInfo k : keys) {
                if (k.key.equals(keyInfo.key)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static class InvalidCounter {
        AtomicInteger count;
        long lastUpdateTime;

        public InvalidCounter(AtomicInteger count, long lastUpdateTime) {
            this.count = count;
            this.lastUpdateTime = lastUpdateTime;
        }
    }
}
