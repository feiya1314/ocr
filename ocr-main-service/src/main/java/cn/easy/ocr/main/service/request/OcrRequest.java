package cn.easy.ocr.main.service.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author : feiya
 * @date : 2022/6/14
 * @description :
 */
@Getter
@Setter
public class OcrRequest {
    @NotBlank(message = "图片不能为空")
    private String base64Img;

    @NotBlank(message = "ocr语言不能为空")
    private String ocrLang;
}
