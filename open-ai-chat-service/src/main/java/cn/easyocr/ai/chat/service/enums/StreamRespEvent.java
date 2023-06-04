package cn.easyocr.ai.chat.service.enums;

/**
 * @author : feiya
 * @date : 2023/5/18
 * @description :
 */
public enum StreamRespEvent {
    FINISH("finish", "完成"),

    ERROR("error", "出错"),

    ;

    private final String event;

    private final String desc;

    StreamRespEvent(String event, String desc) {
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
