package cn.easyocr.ai.chat.service.dao.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/5/28
 * @description :
 */
@Getter
@Setter
@Builder
public class ChatMsgQuery {
    private Long id;
    private String userId;
    private String chatId;
    private String msgId;
    private String nextMsgId;
    private Integer role;
    private Integer ptd;
}
