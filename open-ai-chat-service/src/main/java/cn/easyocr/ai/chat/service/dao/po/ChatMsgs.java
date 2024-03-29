package cn.easyocr.ai.chat.service.dao.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author : feiya
 * @date : 2023/5/27
 * @description :
 */
@Getter
@Setter
public class ChatMsgs {
    private Long id;
    private Long userId;
    /**
     * {@link cn.easyocr.ai.chat.service.enums.ChatGptModel}
     */
    private Integer model;
    private String chatId;
    private String msgId;
    private String nextMsgId;
    private String content;
    /**
     * {@link cn.easyocr.ai.chat.service.enums.ChatRole}
     */
    private Integer role;
    private Date createdTime;
    private Long timestamp;
    private Integer ptd;
}
