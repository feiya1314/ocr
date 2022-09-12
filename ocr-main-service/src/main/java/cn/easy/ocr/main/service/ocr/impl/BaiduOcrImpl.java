package cn.easy.ocr.main.service.ocr.impl;

import cn.easy.ocr.main.service.config.ServiceConfg;
import cn.easy.ocr.main.service.dto.OcrContext;
import cn.easy.ocr.main.service.dto.OcrResult;
import cn.easy.ocr.main.service.enums.BdOcrLangEnum;
import cn.easy.ocr.main.service.exception.OcrServiceException;
import cn.easy.ocr.main.service.ocr.IOcr;
import com.baidu.aip.ocr.AipOcr;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author : feiya
 * @date : 2022/8/21
 * @description : 百度ocr识别源
 */
@Qualifier("BaiduOcrImpl")
@Component
@Slf4j
public class BaiduOcrImpl implements IOcr {
    @Autowired
    private ServiceConfg serviceConfg;

    @Autowired
    private ObjectMapper objectMapper;

    private AipOcr ocrClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("init baidu ocr source");
        ocrClient = new AipOcr(serviceConfg.getBaiduSource().getAppID(), serviceConfg.getBaiduSource().getApiKey(), serviceConfg.getBaiduSource().getSecretKey());
        // 可选：设置网络连接参数
        ocrClient.setConnectionTimeoutInMillis(2000);
        ocrClient.setSocketTimeoutInMillis(60000);
        log.info("init baidu ocr source finish");
        // 也可以直接通过jvm启动参数设置此环境变量
        // System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
    }

    @Override
    public OcrResult ocr(OcrContext context) throws OcrServiceException {
        log.info("baidu ocr start");
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<>();
        options.put("language_type", BdOcrLangEnum.getLangCode(context.getLang()));
        options.put("detect_direction", String.valueOf(context.isDetectLanguage()));
        options.put("detect_language", "false");
        options.put("probability", "true");

        OcrResult result = new OcrResult();
        // 参数为本地图片路径
//        String image = "test.jpg";
//        JSONObject res = ocrClient.basicGeneral(image, options);

        // 参数为本地图片二进制数组

        // 通用文字识别, 图片参数为远程url图片
//        JSONObject res = client.basicGeneralUrl(url, options);
//        System.out.println(res.toString(2));

        JSONObject res = ocrClient.basicGeneral(context.getImg(), options);
        if (res == null) {
            result.setSuccess(false);
            return result;
        }
        JSONArray words = (JSONArray) res.get("words_result");

        StringBuilder sb = new StringBuilder();
        words.forEach(w -> {
            JSONObject jb = (JSONObject) w;
            sb.append(jb.get("words"));
            sb.append(System.lineSeparator());
        });
        log.info("bd ocr result : {}", words);
        result.setImageText(sb.toString());

        return result;
    }

    @Override
    public String convertLanguage(String inputLang) {
        return null;
    }

    @Override
    public int remainingTimes() {
        return 0;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String ocrSourceName() {
        return "baidu-ocr";
    }
}
