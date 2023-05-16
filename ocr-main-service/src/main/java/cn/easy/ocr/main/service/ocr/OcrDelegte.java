package cn.easy.ocr.main.service.ocr;

import cn.easy.ocr.main.service.dto.OcrContext;
import cn.easy.ocr.main.service.enums.OcrLangEnum;
import cn.easy.ocr.main.service.enums.OcrSourceEnum;
import cn.easy.ocr.main.service.exception.OcrServiceException;
import cn.easy.ocr.main.service.request.OcrRequest;
import cn.easyocr.common.utils.Constants;
import cn.easy.ocr.main.service.vo.OcrResultVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
@Service
@Slf4j
public class OcrDelegte {

    @Autowired
    private List<IOcr> ocrSources;

    public OcrResultVo invokeOcr(OcrRequest request) {
        log.info("invoke ocr");
        OcrResultVo vo = new OcrResultVo();
        OcrContext.OcrContextBuilder builder = OcrContext.builder();
        String reqId = MDC.get(Constants.REQUEST_TRACE_KEY);
        builder.requestId(reqId).request(request).lang(OcrLangEnum.getEnumByCode(request.getOcrLang()));
        OcrContext context = builder.build();
        vo.setText(getOcrSource().ocr(context).getImageText());

        return vo;
    }

    private IOcr getOcrSource() {
        // todo 根据ocr优先级
        for (IOcr ocr : ocrSources) {
            if (OcrSourceEnum.PADDLE.getSourceName().equals(ocr.ocrSourceName())) {
                return ocr;
            }
        }
        throw new OcrServiceException("no avaliable ocr source");
    }
}
