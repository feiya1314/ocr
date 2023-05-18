package cn.easyocr.common.resp;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/5/17
 * @description :
 */
@Getter
@Setter
public class BaseResponse<T> {
    private String msg;
    private Integer code;
    private T data;
}
