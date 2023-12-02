package cn.easyocr.ai.chat.service.chat;

import cn.easyocr.ai.chat.service.BaseTest;
import cn.easyocr.ai.chat.service.config.ChatConfig;
import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.context.ChatServiceResult;
import cn.easyocr.ai.chat.service.enums.ChatType;
import cn.easyocr.ai.chat.service.keys.ApiKeyManager;
import cn.easyocr.ai.chat.service.keys.KeyInfo;
import cn.easyocr.ai.chat.service.req.AiChatReq;
import cn.easyocr.ai.chat.service.service.OpenAiChatImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : feiya
 * @date : 2023/10/29
 * @description :
 */
public class OpenAiChatTest extends BaseTest {
    @Autowired
    private ChatConfig config;

    @Autowired
    private ApiKeyManager apiKeyManager;

    @Autowired
    private OpenAiChatImpl openAiChat;

    @Test
    public void chatTest() throws Exception {
        AiChatReq aiChatReq = new AiChatReq();
        ChatContext.ChatContextBuilder chatContextBuilder = ChatContext.builder()
                .aiChatReq(aiChatReq)
                .userId(9527L)
                .chatType(ChatType.getByType(aiChatReq.getChatType()));

        ChatContext chatContext = chatContextBuilder.build();
        KeyInfo keyInfo = apiKeyManager.getApiKey(chatContext.getChatType());
        chatContext.setKeyInfo(keyInfo);

        ChatServiceResult chatServiceResult = openAiChat.chat(chatContext);
        Thread.sleep(50000000L);
    }
}
