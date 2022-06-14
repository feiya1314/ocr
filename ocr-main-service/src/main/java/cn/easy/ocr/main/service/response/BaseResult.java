package cn.easy.ocr.main.service.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2022/6/14
 * @description :
 */
@Getter
@Setter
public class BaseResult<T> {
    private String msg;
    private Integer code;
    private T data;
}
