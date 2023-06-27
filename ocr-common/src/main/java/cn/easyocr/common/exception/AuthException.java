package cn.easyocr.common.exception;

import cn.easyocr.common.enums.ResultCodeEnum;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
public class AuthException extends RuntimeException {
    private ResultCodeEnum resultCodeEnum;

    public AuthException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(ResultCodeEnum resultCodeEnum, String message) {
        super(message);
        this.resultCodeEnum = resultCodeEnum;
    }

    public AuthException(ResultCodeEnum resultCodeEnum, Object... formatParams) {
        super(ResultCodeEnum.formatMsg(resultCodeEnum, formatParams));
        this.resultCodeEnum = resultCodeEnum;
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(ResultCodeEnum resultCodeEnum, String message, Throwable cause) {
        super(message, cause);
        this.resultCodeEnum = resultCodeEnum;
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ResultCodeEnum getResultCodeEnum() {
        return resultCodeEnum;
    }
}
