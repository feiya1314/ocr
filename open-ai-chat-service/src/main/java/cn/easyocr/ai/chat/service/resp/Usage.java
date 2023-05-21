package cn.easyocr.ai.chat.service.resp;

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
public class Usage {
    @JsonProperty("prompt_tokens")
    private long promptTokens;

    @JsonProperty("completion_tokens")
    private long completionTokens;

    @JsonProperty("total_tokens")
    private long totalTokens;
}
