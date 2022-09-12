package cn.easy.ocr.main.service.ocr;

import cn.easy.ocr.main.service.dto.OcrContext;
import cn.easy.ocr.main.service.request.OcrRequest;
import cn.easy.ocr.main.service.vo.OcrResultVo;
import lombok.extern.slf4j.Slf4j;
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
        builder.request(request);
        OcrContext context = builder.build();
        vo.setText(ocrSources.get(0).ocr(context).getImageText());

        return vo;
    }
}
