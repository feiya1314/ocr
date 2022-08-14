package cn.easy.ocr.main.service.service;

import cn.easy.ocr.main.service.dto.OcrContext;
import cn.easy.ocr.main.service.dto.OcrResult;
import cn.easy.ocr.main.service.exception.OcrServiceException;
import org.springframework.core.Ordered;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
public interface IOcr extends Ordered {
    /**
     * 执行ocr识别
     *
     * @param context ocr请求上下文
     * @return ocr识别结果
     * @throws OcrServiceException ocr异常
     */
    OcrResult ocr(OcrContext context) throws OcrServiceException;

    /**
     * ocr服务剩余可用次数，当为0时，ocr服务不可用
     *
     * @return 剩余可用次数
     */
    int remainingTimes();
}
