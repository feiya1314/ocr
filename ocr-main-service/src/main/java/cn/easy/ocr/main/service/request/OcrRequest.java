package cn.easy.ocr.main.service.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2022/6/14
 * @description :
 */
@Getter
@Setter
public class OcrRequest {
    private String base64Img;
    private Integer lang;
}
