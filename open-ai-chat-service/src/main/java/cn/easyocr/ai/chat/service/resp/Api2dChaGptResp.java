package cn.easyocr.ai.chat.service.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description : <a href="https://api2d-doc.apifox.cn/">...</a> API2D 返回的stream 数据流格式
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Api2dChaGptResp {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatChoice> choices;
    private Usage usage;
}
