package cn.easy.ocr.main.service.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author : feiya
 * @date : 2022/10/4
 * @description :
 */
public class TimeUtil {

    private static final DateTimeFormatter ptdFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static Integer getPtd() {
        String ptd = ptdFormatter.format(LocalDateTime.now());
        return Integer.parseInt(ptd);
    }
}
