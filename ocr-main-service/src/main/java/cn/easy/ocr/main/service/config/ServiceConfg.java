package cn.easy.ocr.main.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : feiya
 * @date : 2022/8/14
 * @description : ocr服务配置
 */
@Component
@ConfigurationProperties(prefix = "ocr.service")
@Getter
@Setter
public class ServiceConfg {
    private String paddleSource;

    private BaiduSource baiduSource;

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "ocr.service.baiduSource")
    public static class BaiduSource {
        private String appID;
        private String apiKey;
        private String secretKey;
    }
}
