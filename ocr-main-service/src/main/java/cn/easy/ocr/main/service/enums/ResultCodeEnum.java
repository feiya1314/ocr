package cn.easy.ocr.main.service.enums;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
public enum ResultCodeEnum {
    SUCCESS(0, "成功"),
    ;
    private final int code;
    private final String desc;

    ResultCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
