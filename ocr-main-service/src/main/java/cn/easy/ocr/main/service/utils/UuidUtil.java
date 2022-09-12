package cn.easy.ocr.main.service.utils;

import java.util.UUID;

/**
 * @author : feiya
 * @date : 2022/9/12
 * @description :
 */
public class UuidUtil {
    /**
     * 获取uuid字符串
     *
     * @return uuid字符串
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
