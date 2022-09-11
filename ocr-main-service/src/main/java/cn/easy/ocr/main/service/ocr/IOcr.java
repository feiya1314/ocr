package cn.easy.ocr.main.service.ocr;

import cn.easy.ocr.main.service.dto.OcrContext;
import cn.easy.ocr.main.service.dto.OcrResult;
import cn.easy.ocr.main.service.exception.OcrServiceException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
public interface IOcr extends Ordered, InitializingBean {
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

    /**
     * 将页面输入的识别语言转为对应识别源的语言
     *
     * @param inputLang 输入语言
     * @return 转换后语言
     */
    String convertLanguage(String inputLang);

    /**
     * 获取当前的ocr 名称
     *
     * @return 当前的ocr 名称
     */
    String ocrSourceName();
}
