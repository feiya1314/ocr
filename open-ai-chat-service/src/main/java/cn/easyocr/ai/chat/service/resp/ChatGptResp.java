package cn.easyocr.ai.chat.service.resp;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Getter
@Setter
public class ChatGptResp {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatChoice> choices;
    private Usage usage;
}
