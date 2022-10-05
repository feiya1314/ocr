package cn.easy.ocr.main.service.ocr.impl;

import cn.easy.ocr.main.service.config.ServiceConfg;
import cn.easy.ocr.main.service.dto.OcrContext;
import cn.easy.ocr.main.service.dto.OcrResult;
import cn.easy.ocr.main.service.enums.OcrSourceEnum;
import cn.easy.ocr.main.service.exception.OcrServiceException;
import cn.easy.ocr.main.service.ocr.IOcr;
import cn.easy.ocr.main.service.utils.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description : paddle ocr服务
 */
@Qualifier("PaddleOcrImpl")
@Profile("prod")
@Component
@Slf4j
public class PaddleOcrImpl implements IOcr {
    @Autowired
    private ServiceConfg serviceConfg;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public OcrResult ocr(OcrContext context) throws OcrServiceException {
        log.info("paddle ocr start");
        OcrResult ocrResult = new OcrResult();
        Map<String, String> params = new HashMap<>();
        params.put("base64Img", context.getRequest().getBase64Img());
        params.put("ocrLang", context.getRequest().getOcrLang());

        String text = HttpUtil.doPostForm(serviceConfg.getPaddleSource(), params);
        ocrResult.setImageText(text);

        log.info("paddle ocr finish");
        return ocrResult;
    }

    @Override
    public boolean available() {
        // 自研服务一直可用
        return true;
    }

    @Override
    public String convertLanguage(String inputLang) {
        return null;
    }

    @Override
    public int getOrder() {
        // 自研服务优先级最低
        return LOWEST_PRECEDENCE;
    }

    @Override
    public String ocrSourceName() {
        return OcrSourceEnum.PADDLE.getSourceName();
    }
}
