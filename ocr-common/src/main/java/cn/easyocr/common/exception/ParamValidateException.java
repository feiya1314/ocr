package cn.easyocr.common.exception;

import cn.easyocr.common.enums.ResultCodeEnum;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
public class ParamValidateException extends RuntimeException {
    private ResultCodeEnum resultCodeEnum;

    public ParamValidateException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
    }

    public ParamValidateException(String message) {
        super(message);
    }

    public ParamValidateException(ResultCodeEnum resultCodeEnum, String message) {
        super(message);
        this.resultCodeEnum = resultCodeEnum;
    }

    public ParamValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamValidateException(ResultCodeEnum resultCodeEnum, String message, Throwable cause) {
        super(message, cause);
        this.resultCodeEnum = resultCodeEnum;
    }

    public ParamValidateException(Throwable cause) {
        super(cause);
    }

    public ParamValidateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
