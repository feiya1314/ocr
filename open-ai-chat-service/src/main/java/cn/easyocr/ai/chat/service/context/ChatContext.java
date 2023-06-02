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

    /**
     * 会话id
     */
    private String chatId;

    /**
     * 当次请求的记录的id
     */
    private String reqMsgId;

    /**
     * 当次请求的记录的上个ai回答的id
     */
    private String parentMsgId;

    /**
     * 当次请求的ai回答的id
     */
    private String respMsgId;

    private List<Message> reqMessages;
}
