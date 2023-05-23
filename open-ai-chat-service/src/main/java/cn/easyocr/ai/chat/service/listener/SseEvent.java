package cn.easyocr.ai.chat.service.listener;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/5/22
 * @description :
 */
@Getter
@Setter
@Builder
public class SseEvent {
    /**
     * 声明事件的标识符。服务器端返回的数据中包含事件的标识符，浏览器会记录最近一次接收的事件的标识符。如果与服务器的连接中断
     * ，浏览器再次连接时，会通过HTTP头部的Last-Event-ID来声明最后一次接收到的事件的标识符，服务器端可以根据id，决定从哪个事件继续
     */
    private String id;
    private String event;
    private String data;
    private Integer retry;
}
