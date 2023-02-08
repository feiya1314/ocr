package cn.easy.ocr.main.service.redis;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

/**
 * @author : feiya
 * @description :
 * @since : 2023/2/5
 */
@Getter
@Setter
public class TimeWindow<T> {
    private int count = 0;
    private long leftTime = 0L;
    private long rightTime = 0L;
    private long windowSize = 2000;
    private int limitCount = 0;
    private LinkedList<Slice<T>> slices;
    private String lastResult;
    private String key;

    public void slide(Slice<T> slice) {

    }
}
