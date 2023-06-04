package cn.easyocr.ai.chat.service.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/5/25
 * @description :
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatOptions {
    private String conversationId;

    private String parentMessageId;
}
