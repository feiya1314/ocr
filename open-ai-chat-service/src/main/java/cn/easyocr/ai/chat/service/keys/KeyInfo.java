package cn.easyocr.ai.chat.service.keys;

import cn.easyocr.ai.chat.service.enums.ChatSource;

/**
 * @author : feiya
 * @date : 2023/10/21
 * @description :
 */
public class KeyInfo {
    public ChatSource chatSource;
    public String key;

    public int status;

    public String invalidInfo;

    public KeyInfo(String key, ChatSource chatSource) {
        this.key = key;
        this.chatSource = chatSource;
    }

    public void resetKeyAvailable() {
        status = 0;
        invalidInfo = null;
    }
}
