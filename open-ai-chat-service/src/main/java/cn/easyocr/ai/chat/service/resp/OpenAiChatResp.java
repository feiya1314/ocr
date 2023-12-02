package cn.easyocr.ai.chat.service.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description : <a href="https://platform.openai.com/docs/api-reference/chat/streaming">...</a>
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpenAiChatResp {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatChoice> choices;
}
