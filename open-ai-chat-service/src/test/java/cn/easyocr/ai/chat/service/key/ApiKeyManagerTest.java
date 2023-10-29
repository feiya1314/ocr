package cn.easyocr.ai.chat.service.key;

import cn.easyocr.ai.chat.service.BaseTest;
import cn.easyocr.ai.chat.service.enums.ChatType;
import cn.easyocr.ai.chat.service.keys.ApiKeyManager;
import cn.easyocr.ai.chat.service.keys.KeyInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : feiya
 * @date : 2023/10/29
 * @description :
 */
public class ApiKeyManagerTest extends BaseTest {
    @Autowired
    private ApiKeyManager apiKeyManager;

    @Test
    public void testGetApiKey() throws Exception {
        ChatType chatType = ChatType.CHAT_GPT;
        KeyInfo keyInfo = apiKeyManager.getApiKey(chatType);
        Assertions.assertNotNull(keyInfo);
        Assertions.assertTrue(keyInfo.key.startsWith("dev"));
    }

    @Test
    public void testInvalidKey() throws Exception {
        ChatType chatType = ChatType.CHAT_GPT;
        KeyInfo keyInfo = apiKeyManager.getApiKey(chatType);
        Assertions.assertNotNull(keyInfo);
        Assertions.assertTrue(keyInfo.key.startsWith("dev"));
        apiKeyManager.invalidKey(chatType, keyInfo);
        apiKeyManager.invalidKey(chatType, keyInfo);
        apiKeyManager.invalidKey(chatType, keyInfo);
        apiKeyManager.invalidKey(chatType, keyInfo);

        KeyInfo keyInfo2 = apiKeyManager.getApiKey(chatType);
        Assertions.assertNotNull(keyInfo2);
        Assertions.assertTrue(keyInfo2.key.startsWith("dev"));
        apiKeyManager.invalidKey(chatType, keyInfo2);
        apiKeyManager.invalidKey(chatType, keyInfo2);
        apiKeyManager.invalidKey(chatType, keyInfo2);
        apiKeyManager.invalidKey(chatType, keyInfo2);

        KeyInfo keyInfo3 = apiKeyManager.getApiKey(chatType);
        Assertions.assertNotNull(keyInfo3);
        Assertions.assertTrue(keyInfo3.key.startsWith("dev"));
        apiKeyManager.invalidKey(chatType, keyInfo3);
        apiKeyManager.invalidKey(chatType, keyInfo3);
        apiKeyManager.invalidKey(chatType, keyInfo3);
        apiKeyManager.invalidKey(chatType, keyInfo3);

        KeyInfo keyInfo4 = apiKeyManager.getApiKey(chatType);
        Assertions.assertNotNull(keyInfo4);
        Assertions.assertFalse(keyInfo4.key.startsWith("dev"));

    }
}
