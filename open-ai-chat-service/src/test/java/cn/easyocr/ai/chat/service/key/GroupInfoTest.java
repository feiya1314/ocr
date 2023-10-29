package cn.easyocr.ai.chat.service.key;

import cn.easyocr.ai.chat.service.enums.ChatSource;
import cn.easyocr.ai.chat.service.keys.GroupInfo;
import cn.easyocr.ai.chat.service.keys.KeyInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author : feiya
 * @date : 2023/10/22
 * @description :
 */
public class GroupInfoTest {
    @Test
    public void testGroupInfo() throws Exception {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.chatSource = ChatSource.OPEN_AI;
        List<KeyInfo> availableKeys = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            availableKeys.add(getKeyInfo(i));
        }

        groupInfo.availableKeys = availableKeys;
        groupInfo.priority = 1;
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(2000);
        getKeyInfo(groupInfo, countDownLatch);
        invalidKeyTest(groupInfo, countDownLatch);

        countDownLatch.await();
        System.out.println(System.currentTimeMillis() - start);
    }

    private void getKeyInfo(GroupInfo groupInfo, CountDownLatch countDownLatch) throws Exception {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                groupInfo.randomKeyInfo();
                countDownLatch.countDown();
            }).start();
        }
    }

    private void invalidKeyTest(GroupInfo groupInfo, CountDownLatch countDownLatch) throws Exception {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                groupInfo.invalidKey(groupInfo.randomKeyInfo());
                countDownLatch.countDown();
            }).start();
        }
    }

    private KeyInfo getKeyInfo(int i) {
        return new KeyInfo(UUID.randomUUID().toString() + i, ChatSource.OPEN_AI);
    }
}
