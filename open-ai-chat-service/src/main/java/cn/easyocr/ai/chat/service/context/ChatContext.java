package cn.easyocr.ai.chat.service.context;

import cn.easyocr.ai.chat.service.req.AiChatReq;
import cn.easyocr.ai.chat.service.req.Message;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    private List<Message> reqMessages;
}
