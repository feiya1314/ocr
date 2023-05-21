package cn.easyocr.ai.chat.service.listener;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/5/22
 * @description :
 */
@Getter
@Setter
public class SseEvent {
    private String id;
    private String event;
    private String data;
    private Integer retry;
}
