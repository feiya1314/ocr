package cn.easy.ocr.main.service.service;

import cn.easy.ocr.main.service.dto.OcrContext;
import cn.easy.ocr.main.service.dto.OcrResult;
import cn.easy.ocr.main.service.exception.OcrServiceException;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
public interface IOcr {
    OcrResult ocr(OcrContext context) throws OcrServiceException;
}
