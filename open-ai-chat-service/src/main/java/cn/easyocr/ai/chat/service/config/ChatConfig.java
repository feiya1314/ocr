package cn.easyocr.ai.chat.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "chat")
@Component
public class ChatConfig {
    private int maxTokens;

    /**
     * 一个会话最大的对话数，超过需要开启一个新对话
     */
    private int contextMaxMsgs;

    private Api2D api2D;

    private ChatGpt chatGpt;

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "chat.chatGpt")
    public static class ChatGpt {
        private String apiKey;
        private String url;
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "chat.api2D")
    public static class Api2D {
        private String apiKey;
        private String url;
    }
}
