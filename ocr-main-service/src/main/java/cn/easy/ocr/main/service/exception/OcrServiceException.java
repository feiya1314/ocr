package cn.easy.ocr.main.service.exception;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
public class OcrServiceException extends RuntimeException {
    public OcrServiceException() {
    }

    public OcrServiceException(String message) {
        super(message);
    }

    public OcrServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public OcrServiceException(Throwable cause) {
        super(cause);
    }

    public OcrServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
