package cn.easyocr.ai.chat.service.enums;

/**
 * @author : feiya
 * @date : 2023/5/18
 * @description :
 */
public enum StreamEventState {
    FINISH("finish", "完成"),
    CLOSE("close", "stream关闭"),
    EMPTY("empty", "stream空数据"),

    ERROR("error", "出错"),

    ;

    private final String event;

    private final String desc;

    StreamEventState(String event, String desc) {
        this.event = event;

        this.desc = desc;
    }

    public String getEvent() {
        return event;
    }

    public String getDesc() {
        return desc;
    }
}
