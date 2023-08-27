package cn.easyocr.ai.chat.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : feiya
 * @date : 2023/8/13
 * @description :
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "auth")
@Component
public class AuthConfig {
    private String genTokenSecret;
    private long tokenExpiration = 86400000L;

    private String ydSecret;

    private WxConfig wxConfig;

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "auth.wxConfig")
    public static class WxConfig {
        private String appid;
        private String secret;
    }
}
