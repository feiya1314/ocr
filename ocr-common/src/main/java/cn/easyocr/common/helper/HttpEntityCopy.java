package cn.easyocr.common.helper;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/8/25
 * @description :
 */
@Getter
@Setter
public class HttpEntityCopy {
    private byte[] data;
    private String contentType;
    private String contentEncoding;
}
