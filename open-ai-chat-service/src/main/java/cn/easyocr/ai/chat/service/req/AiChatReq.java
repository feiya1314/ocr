package cn.easyocr.ai.chat.service.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author : feiya
 * @date : 2023/5/17
 * @description :
 */
@Getter
@Setter
public class AiChatReq {
    @NotBlank(message = "系统信息不能为空")
    private String systemMessage;

    @NotBlank(message = "问题不能为空")
    private String prompt;

    private ChatOptions options;

    private Integer temperature;

    private Integer topP;

    @Getter
    @Setter
    public static class ChatOptions {
        private String conversationId;

        private String parentMessageId;
    }
}
