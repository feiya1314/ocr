package cn.easy.ocr.main.service.redis;

import cn.easyocr.common.redis.JedisManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;

/**
 * @author : feiya
 * @description :
 * @since : 2023/2/5
 */
public class TimeWindowManager {
    private JedisManager jedisManager;

    private long rightTime = 0L;

    private long windowSize = 2000;

    private int limitCount = 10;

    @Autowired
    public TimeWindowManager(JedisManager jedisManager) {
        this.jedisManager = jedisManager;
    }

    public <T> boolean slide(TimeWindow<T> window, Slice<T> cur) {
        String key = window.getKey();
        LinkedList<Slice<T>> slices = window.getSlices();
        if (slices == null) {
            slices = new LinkedList<>();
            window.setSlices(slices);
            slices.offer(cur);
            return true;
        }

        long windowSize = window.getWindowSize();
        Slice<T> sliceHead = slices.peek();

        if (sliceHead == null) {
            slices.offer(cur);
            return true;
        }

        long distance = cur.getTimestamps() - sliceHead.getTimestamps();
        // 在窗口范围内，并且频率没超过限制，则将当前的请求放到时间窗口的 slices 尾部
        if (distance < windowSize && slices.size() < limitCount) {
            slices.offer(cur);
            return true;
        }

        // 在窗口范围内，并且频率超过了限制，则将当前的请求放到时间窗口的 slices 尾部
        // 并且返回超过限制
        if (distance < windowSize) {
            slices.offer(cur);
            // todo 限制后续请求
            return false;
        }

        // 下面都是超出了时间窗口范围的情况
        if (slices.size() >= limitCount) {
            // todo 正常情况下，不应该有这种现象，限制后续请求，
            return false;
        }

        slices.offer(cur);
        pickOutDeprecatedSlice(slices, cur, windowSize);
        return true;
    }

    private <T> void pickOutDeprecatedSlice(LinkedList<Slice<T>> slices, Slice<T> cur, long windowSize) {
        Slice<T> head = slices.peek();
        while (head != null && cur.getTimestamps() - head.getTimestamps() > windowSize) {
            slices.poll();
            head = slices.peek();
        }
    }
}
