package cn.easyocr.common.exception;

import cn.easyocr.common.enums.ResultCodeEnum;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
public class ServiceException extends RuntimeException {
    private ResultCodeEnum resultCodeEnum;

    public ServiceException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(ResultCodeEnum resultCodeEnum, String message) {
        super(message);
        this.resultCodeEnum = resultCodeEnum;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(ResultCodeEnum resultCodeEnum, String message, Throwable cause) {
        super(message, cause);
        this.resultCodeEnum = resultCodeEnum;
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ResultCodeEnum getResultCodeEnum() {
        return resultCodeEnum;
    }
}
