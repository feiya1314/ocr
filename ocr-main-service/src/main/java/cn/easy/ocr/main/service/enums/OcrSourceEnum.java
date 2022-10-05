package cn.easy.ocr.main.service.enums;

/**
 * @author : feiya
 * @date : 2022/10/5
 * @description :
 */
public enum OcrSourceEnum {
    PADDLE("paddle-ocr", "paddle通用ocr识别"),
    BAIDU_GENERAL("baidu-gen-ocr", "百度通用ocr识别"),
    ;
    private final String sourceName;
    private final String desc;

    OcrSourceEnum(String sourceName, String desc) {
        this.sourceName = sourceName;
        this.desc = desc;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDesc() {
        return desc;
    }
}
