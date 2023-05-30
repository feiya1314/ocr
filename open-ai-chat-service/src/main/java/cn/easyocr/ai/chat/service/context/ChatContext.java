package cn.easyocr.ai.chat.service.context;

import cn.easyocr.ai.chat.service.req.AiChatReq;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/5/18
 * @description :
 */
@Getter
@Setter
@Builder
public class ChatContext {
    private AiChatReq aiChatReq;

    private String chatId;

    private String reqMsgId;

    private String parentMsgId;

    private String respMsgId;
}
