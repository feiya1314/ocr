package cn.easyocr.ai.chat.service.resp;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/5/17
 * @description :
 */
@Getter
@Setter
public class AiChatMsgResp {
    private String id;
    private String text;

    /**
     * {@link cn.easyocr.ai.chat.service.enums.ChatRole}
     */
    private String role;
    private String name;
    private String delta;
    private String detail;
    private String parentMessageId;
    private String conversationId;
}
