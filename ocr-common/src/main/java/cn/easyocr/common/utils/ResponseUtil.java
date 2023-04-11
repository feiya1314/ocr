package cn.easyocr.common.utils;

import cn.easyocr.common.enums.ResultCodeEnum;
import cn.easyocr.common.resp.ErrorResponse;

/**
 * @author : feiya
 * @description :
 * @since : 2023/4/11
 */
public class ResponseUtil {
    public static ErrorResponse fail(ResultCodeEnum resultCodeEnum) {
        return new ErrorResponse(resultCodeEnum.getCode(), resultCodeEnum.getDesc());
    }

    public static ErrorResponse fail(int resultCode, String msg) {
        return new ErrorResponse(resultCode, msg);
    }
}
