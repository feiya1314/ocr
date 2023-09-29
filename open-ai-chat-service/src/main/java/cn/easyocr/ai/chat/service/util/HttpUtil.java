package cn.easyocr.ai.chat.service.util;

import cn.easyocr.common.utils.Constants;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : feiya
 * @date : 2023/9/29
 * @description :
 */
public class HttpUtil {
    public static Long getUserId(HttpServletRequest request) {
        String userId = request.getHeader(Constants.REQ_USER_ID);
        return Long.parseLong(userId);
    }
}
