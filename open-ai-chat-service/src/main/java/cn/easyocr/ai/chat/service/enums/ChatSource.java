package cn.easyocr.ai.chat.service.enums;

/**
 * @author : feiya
 * @date : 2023/10/21
 * @description :
 */
public enum ChatSource {
    OPEN_AI(ChatType.CHAT_GPT, ChatSourceKey.OPEN_AI, true),
    API_2D(ChatType.CHAT_GPT, ChatSourceKey.API_2D, true),
    ALI(ChatType.ALI, ChatSourceKey.ALI, false),
    ;

    private final ChatType chatType;
    private final String source;
    private final boolean enabled;

    ChatSource(ChatType chatType, String source, boolean enabled) {
        this.source = source;
        this.chatType = chatType;
        this.enabled = enabled;
    }

    public String getSource() {
        return source;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
