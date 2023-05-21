package cn.easyocr.ai.chat.service.resp;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/5/17
 * @description :
 */
@Getter
@Setter
public class AiChatMsgResp {
    private String id;
    private String text;

    /**
     * {@link cn.easyocr.ai.chat.service.enums.ChatRole}
     */
    private String role;
    private String name;
    private String delta;
    private MsgDetails detail;
    private String parentMessageId;
    private String conversationId;

    @Getter
    @Setter
    public static class MsgDetails {
        private String id;
        private String object;
        private String model;
        private Choices choices;
        private Long created;
    }

    @Getter
    @Setter
    public static class Choices {
        private Delta delta;
        private Integer index;
        private String finish_reason;
    }

    @Getter
    @Setter
    public static class Delta {
        private String content;
    }
}
