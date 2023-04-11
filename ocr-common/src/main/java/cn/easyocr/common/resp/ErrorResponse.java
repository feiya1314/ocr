package cn.easyocr.common.resp;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : feiya
 * @description :
 * @since : 2023/4/11
 */
@Getter
@Setter
public class ErrorResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -180966517858163875L;
    private int errorCode;
    private String errorMsg;

    public ErrorResponse(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
