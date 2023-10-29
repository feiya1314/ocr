package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.context.ChatServiceResult;
import cn.easyocr.ai.chat.service.enums.ChatSource;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
public interface IAiChatService {
    ChatServiceResult chat(ChatContext chatContext);

    ChatSource getChatSource();
}
