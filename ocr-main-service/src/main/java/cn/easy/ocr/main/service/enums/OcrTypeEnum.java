package cn.easy.ocr.main.service.enums;

/**
 * @author : feiya
 * @date : 2022/9/12
 * @description :
 */
public enum OcrTypeEnum {
    GENERAL(0, "通用ocr识别");
    private final int type;
    private final String desc;

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    OcrTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
