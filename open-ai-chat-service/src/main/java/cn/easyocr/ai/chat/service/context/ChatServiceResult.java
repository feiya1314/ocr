package cn.easyocr.ai.chat.service.context;

import cn.easyocr.ai.chat.service.resp.AiChatMsgResp;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * @author : feiya
 * @date : 2023/5/25
 * @description :
 */
@Getter
@Setter
public class ChatServiceResult {
    private AiChatMsgResp aiChatMsgResp;

    private StreamingResponseBody streamingResponseBody;
}
