package cn.easyocr.ai.chat.service.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plexpt.chatgpt.entity.chat.Message;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Getter
@Setter
public class ChatChoice {
    private long index;

    @JsonProperty("delta")
    private Message delta;

    @JsonProperty("message")
    private Message message;

    @JsonProperty("finish_reason")
    private String finishReason;
}
