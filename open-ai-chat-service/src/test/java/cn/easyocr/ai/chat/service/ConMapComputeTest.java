package cn.easyocr.ai.chat.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : feiya
 * @date : 2023/10/22
 * @description :
 */
public class ConMapComputeTest {
    private final static Map<String, AtomicInteger> validCounters = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 2000; i++) {
            new Thread(ConMapComputeTest::compute).start();
        }

        Thread.sleep(10000);
        System.out.println(validCounters.get("test").get());
    }

    private static void compute() {
        validCounters.compute("test", (k, counter) -> {
           // synchronized (validCounters) {
                if (counter == null) {
                    return new AtomicInteger(1);
                }
            //}
            counter.incrementAndGet();
            return counter;
        });

        validCounters.computeIfPresent("test", (k, counter) -> {
            counter.incrementAndGet();
            return counter;
        });
    }
}
