package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.req.ChatContext;
import cn.easyocr.ai.chat.service.resp.AiChatMsgResp;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
public interface IAiChatService {
    AiChatMsgResp chat(ChatContext chatContext);
}
