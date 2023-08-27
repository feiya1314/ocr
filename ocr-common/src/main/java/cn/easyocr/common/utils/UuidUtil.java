package cn.easyocr.common.utils;

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

    public static String gen9Uuid() {
        String id = getUuid();
        return id.substring(id.length() - 8);
    }

    public static void main(String[] args) {
        System.out.println(gen9Uuid());
    }
}
