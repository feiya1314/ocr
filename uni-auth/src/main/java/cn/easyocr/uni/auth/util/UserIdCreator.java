package cn.easyocr.uni.auth.util;

import java.util.UUID;

/**
 * @author : feiya
 * @date : 2023/6/18
 * @description :
 */
public class UserIdCreator {

    public static long generateUniqueId() {
        long id;

        UUID uuid = UUID.randomUUID();
        long hash = uuid.hashCode();
        id = hash < 0 ? -hash : hash; // 取绝对值
        id = id % 1000000000; // 取后9位数
        return id;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(generateUniqueId());
        }
    }
}
