package cn.easyocr.ai.chat.service.req;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/5/25
 * @description :
 */
@Getter
@Setter
public class ChatOptions {
    private String conversationId;

    private String parentMessageId;
}
