package cn.easyocr.ai.chat.service.keys;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author : feiya
 * @date : 2023/10/21
 * @description : apiKey分组，每种ChatType 都会有一个分组，分组中可能有多个不同的chat 源，例如chatgpt 和 chatgpt的代理接口源
 */
public class ApiKeyGroup {
    private final Queue<GroupInfo> availableGroups = new PriorityQueue<>(Comparator.comparingInt(o -> o.priority));

    public ApiKeyGroup(List<GroupInfo> groups) {
        availableGroups.addAll(groups);
    }

    public KeyInfo nextKey() {
        if (availableGroups.isEmpty()) {
            throw new RuntimeException("no available keys");
        }
        GroupInfo groupInfo = findAvailableGroupInfo();

        return groupInfo.randomKeyInfo();
    }

    private GroupInfo findAvailableGroupInfo() {
        while (!availableGroups.isEmpty()) {
            GroupInfo groupInfo = availableGroups.peek();
            if (groupInfo.availableKeys != null && !groupInfo.availableKeys.isEmpty()) {
                return groupInfo;
            }

            availableGroups.poll();
        }

        throw new RuntimeException("no available keys");
    }

    public void validKey(KeyInfo keyInfo) {
        for (GroupInfo group : availableGroups) {
            if (group.chatSource == keyInfo.chatSource) {
                group.invalidKey(keyInfo);
            }
        }
    }
}
