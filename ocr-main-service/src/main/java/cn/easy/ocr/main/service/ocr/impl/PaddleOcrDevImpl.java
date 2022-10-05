package cn.easy.ocr.main.service.ocr.impl;

import cn.easy.ocr.main.service.config.ServiceConfg;
import cn.easy.ocr.main.service.dto.OcrContext;
import cn.easy.ocr.main.service.dto.OcrResult;
import cn.easy.ocr.main.service.enums.OcrSourceEnum;
import cn.easy.ocr.main.service.exception.OcrServiceException;
import cn.easy.ocr.main.service.ocr.IOcr;
import cn.easy.ocr.main.service.response.BaseResult;
import cn.easy.ocr.main.service.utils.HttpUtil;
import cn.easy.ocr.main.service.vo.OcrResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description : paddle ocr服务,用于测试
 */
@Qualifier("PaddleOcrImpl")
@Profile("dev")
@Component
@Slf4j
public class PaddleOcrDevImpl implements IOcr {
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

        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            log.error("get json string error", e);
            throw new RuntimeException(e);
        }
        String text = HttpUtil.doPostJson(serviceConfg.getPaddleSource(), jsonBody);
        if (StringUtils.hasText(text)) {
            try {
                BaseResult<OcrResultVo> result = objectMapper.readValue(text,
                        new TypeReference<BaseResult<OcrResultVo>>() {
                });

                ocrResult.setImageText(result.getData().getText());
                log.info("paddle ocr finish");
                return ocrResult;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
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
