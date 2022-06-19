package cn.easy.ocr.main.service.service.impl;

import cn.easy.ocr.main.service.dto.OcrContext;
import cn.easy.ocr.main.service.dto.OcrResult;
import cn.easy.ocr.main.service.exception.OcrServiceException;
import cn.easy.ocr.main.service.service.IOcr;
import cn.easy.ocr.main.service.utils.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
@Component
public class PaddleOcrImpl implements IOcr {
    @Value("${ocr.paddle.source.url:https://www.easyocr.cn/ocr}")
    private String paddleOcrUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public OcrResult ocr(OcrContext context) throws OcrServiceException {
        OcrResult ocrResult = new OcrResult();
        Map<String, String> params = new HashMap<>();
        params.put("base64Img", context.getRequest().getBase64Img());
        params.put("ocrLang", context.getRequest().getOcrLang());
        String text = HttpUtil.doPostForm(paddleOcrUrl, params);
        ocrResult.setImageText(text);

        return ocrResult;
    }
}
