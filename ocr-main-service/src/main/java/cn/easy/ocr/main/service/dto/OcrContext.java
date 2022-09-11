package cn.easy.ocr.main.service.dto;

import cn.easy.ocr.main.service.enums.OcrLangEnum;
import cn.easy.ocr.main.service.request.OcrRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
@Getter
@Setter
@Builder
public class OcrContext {
    private OcrRequest request;
    private byte[] img;
    private boolean detectLanguage;
    private OcrLangEnum lang;
}
