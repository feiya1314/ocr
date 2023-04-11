package cn.easy.ocr.main.service.exception;

import cn.easyocr.common.enums.ResultCodeEnum;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
public class OcrServiceException extends RuntimeException {
    private ResultCodeEnum resultCodeEnum;

    public OcrServiceException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
    }

    public OcrServiceException(String message) {
        super(message);
    }

    public OcrServiceException(ResultCodeEnum resultCodeEnum, String message) {
        super(message);
        this.resultCodeEnum = resultCodeEnum;
    }

    public OcrServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public OcrServiceException(ResultCodeEnum resultCodeEnum, String message, Throwable cause) {
        super(message, cause);
        this.resultCodeEnum = resultCodeEnum;
    }

    public OcrServiceException(Throwable cause) {
        super(cause);
    }

    public OcrServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
