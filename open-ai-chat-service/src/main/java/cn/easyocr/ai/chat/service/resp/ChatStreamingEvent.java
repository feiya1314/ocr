package cn.easyocr.ai.chat.service.resp;

import cn.easyocr.ai.chat.service.enums.StreamEventState;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/10/28
 * @description : chat stream event ,打字效果，后端会不断返回书籍
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatStreamingEvent<R> {
    /**
     * streamming 状态，结束或者出错
     */
    private StreamEventState state;

    private R data;

    private String msg;
}
