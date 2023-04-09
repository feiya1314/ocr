package cn.easy.ocr.main.service.ocr;

import cn.easy.ocr.main.service.dto.OcrContext;
import cn.easy.ocr.main.service.dto.OcrResult;
import cn.easy.ocr.main.service.enums.OcrLangEnum;
import cn.easy.ocr.main.service.ocr.impl.BaiduOcrImpl;
import cn.easy.ocr.main.service.BaseTest;
import cn.easy.ocr.main.service.request.OcrRequest;
import cn.easyocr.common.utils.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : feiya
 * @date : 2022/9/5
 * @description :
 */
public class BaiduOcrTest extends BaseTest {
    @Autowired
    private BaiduOcrImpl baiduOcr;

    @Test
    public void TestOcr() throws Exception {
        OcrContext.OcrContextBuilder builders = OcrContext.builder();

        byte[] img = FileUtil.readResourceFile("text.png");
        OcrRequest request = new OcrRequest();
        //request.setBase64Img();
        builders.request(request).img(img).detectLanguage(false).lang(OcrLangEnum.CH);
        OcrContext context = builders.build();
        OcrResult result = baiduOcr.ocr(context);
        System.out.println(result);
    }
}
