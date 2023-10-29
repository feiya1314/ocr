package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.config.ChatConfig;
import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.context.ChatServiceResult;
import cn.easyocr.ai.chat.service.dao.mapper.ChatMsgsMapper;
import cn.easyocr.ai.chat.service.enums.ChatSource;
import cn.easyocr.ai.chat.service.keys.ApiKeyManager;
import cn.easyocr.ai.chat.service.keys.KeyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Component("aiChatServiceAdapter")
public class AiChatServiceAdapter implements IAiChatService {
    @Autowired
    private ChatConfig config;

    @Autowired
    private Map<String, IAiChatService> api2DChatServices;

    @Autowired
    private ChatMsgsMapper chatMsgsMapper;

    @Autowired
    private ApiKeyManager apiKeyManager;

    @Override
    public ChatSource getChatSource() {
        return null;
    }

    @Override
    public ChatServiceResult chat(ChatContext chatContext) {
        KeyInfo keyInfo = apiKeyManager.getApiKey(chatContext.getChatType());
        chatContext.setKeyInfo(keyInfo);

        return api2DChatServices.get(keyInfo.chatSource.getSource()).chat(chatContext);
    }
}
