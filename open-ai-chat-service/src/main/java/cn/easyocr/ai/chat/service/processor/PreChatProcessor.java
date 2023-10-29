package cn.easyocr.ai.chat.service.processor;

import cn.easyocr.ai.chat.service.context.ChatContext;

/**
 * @author : feiya
 * @date : 2023/10/29
 * @description :
 */
public interface PreChatProcessor {
    void preChat(ChatContext chatContext);
}
