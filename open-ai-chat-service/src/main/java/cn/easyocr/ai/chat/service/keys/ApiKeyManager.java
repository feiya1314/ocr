package cn.easyocr.ai.chat.service.keys;

import cn.easyocr.ai.chat.service.enums.ChatType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2023/10/20
 * @description : api key管理
 */
@Component
@Slf4j
public class ApiKeyManager {
    private final Map<ChatType, ApiKeyGroup> keyGroups = new HashMap<>();
    private final ApiKeyLoader apiKeyLoader;

    public ApiKeyManager(ApiKeyLoader apiKeyLoader) {
        this.apiKeyLoader = apiKeyLoader;
    }

    /**
     * 加载key配置
     */
    @PostConstruct
    public void init() {
        Map<ChatType, List<GroupInfo>> apiKeyGroups = apiKeyLoader.load();
        if (apiKeyGroups == null || apiKeyGroups.isEmpty()) {
            throw new RuntimeException("api key is empty");
        }
        apiKeyGroups.forEach((chatType, groups) -> {
            ApiKeyGroup apiKeyGroup = new ApiKeyGroup(groups);
            keyGroups.put(chatType, apiKeyGroup);
        });
    }

    /**
     * 获取apiKey
     *
     * @return KeyInfo
     */
    public KeyInfo getApiKey(ChatType chatType) {
        ApiKeyGroup apiKeyGroup = keyGroups.get(chatType);
        if (apiKeyGroup == null) {
            throw new RuntimeException("no available keys for given chatType");
        }

        return apiKeyGroup.nextKey();
    }

    /**
     * 剔除失效的apiKey
     */
    public void invalidKey(ChatType chatType, KeyInfo keyInfo) {
        ApiKeyGroup apiKeyGroup = keyGroups.get(chatType);
        if (apiKeyGroup == null) {
            log.warn("no available keys for given chatType :{} , and key :{}", chatType.name(), keyInfo.key);
            return;
        }
        apiKeyGroup.validKey(keyInfo);
    }
}
