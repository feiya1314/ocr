package cn.easyocr.ai.chat.service.enums;

/**
 * @author : feiya
 * @date : 2023/10/21
 * @description :
 */
public enum ChatType {
    CHAT_GPT(1),
    BAI_DU(2),
    ALI(3),
    ;
    private final int type;

    ChatType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static ChatType getByType(int type) {
        for (ChatType chatType : ChatType.values()) {
            if (chatType.type == type) {
                return chatType;
            }
        }
        return null;
    }
}
