package cn.easyocr.ai.chat.service.keys;

import cn.easyocr.ai.chat.service.enums.ChatType;

import java.util.List;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2023/10/22
 * @description :
 */
public interface ApiKeyLoader {
    /**
     * 加载api key
     *
     * @return api key
     */
    Map<ChatType, List<GroupInfo>> load();
}
