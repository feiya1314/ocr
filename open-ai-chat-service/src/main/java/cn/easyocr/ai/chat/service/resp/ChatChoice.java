package cn.easyocr.ai.chat.service.resp;

import cn.easyocr.ai.chat.service.req.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatChoice {
    private long index;

    @JsonProperty("delta")
    private Message delta;

    @JsonProperty("message")
    private Message message;

    @JsonProperty("finish_reason")
    private String finishReason;
}
