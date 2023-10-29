package cn.easyocr.ai.chat.service.keys;

import cn.easyocr.ai.chat.service.config.ChatConfig;
import cn.easyocr.ai.chat.service.enums.ChatSource;
import cn.easyocr.ai.chat.service.enums.ChatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2023/10/22
 * @description :
 */
@Component
public class ConfigApiKeyLoader implements ApiKeyLoader {
    @Autowired
    private ChatConfig chatConfig;

    @Override
    public Map<ChatType, List<GroupInfo>> load() {
        Map<ChatType, List<GroupInfo>> result = new HashMap<>();

        List<GroupInfo> groupInfos = new ArrayList<>();
        groupInfos.add(loadGptConfig());
        groupInfos.add(loadApi2dConfig());

        result.put(ChatType.CHAT_GPT, groupInfos);
        return result;
    }

    private GroupInfo loadGptConfig() {
        ChatConfig.ChatGpt chatGpt = chatConfig.getChatGpt();
        GroupInfo chatGptGroup = new GroupInfo();
        chatGptGroup.chatSource = ChatSource.OPEN_AI;
        chatGptGroup.priority = chatGpt.getPriority();
        chatGptGroup.availableKeys = getKeyInfos(chatGpt.getApiKey(), ChatSource.OPEN_AI);

        return chatGptGroup;
    }

    private GroupInfo loadApi2dConfig() {
        ChatConfig.Api2D api2D = chatConfig.getApi2D();
        GroupInfo chatGptGroup = new GroupInfo();
        chatGptGroup.chatSource = ChatSource.API_2D;
        chatGptGroup.priority = api2D.getPriority();
        chatGptGroup.availableKeys = getKeyInfos(api2D.getApiKey(), ChatSource.API_2D);

        return chatGptGroup;
    }

    private List<KeyInfo> getKeyInfos(String key, ChatSource chatSource) {
        List<KeyInfo> keyInfos = new ArrayList<>();
        String[] keys = key.split(",");
        for (String k : keys) {
            KeyInfo keyInfo = new KeyInfo(k, chatSource);
            keyInfos.add(keyInfo);
        }
        return keyInfos;
    }
}
