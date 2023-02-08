package cn.easy.ocr.main.service.redis;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @description :
 * @since : 2023/2/5
 */
@Getter
@Setter
public class Slice<T> {
    private long timestamps;
    private T data;
}
