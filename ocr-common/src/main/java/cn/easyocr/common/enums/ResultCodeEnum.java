package cn.easyocr.common.enums;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
public enum ResultCodeEnum {
    SUCCESS(0, "success", "成功"),
    PARAM_ERROR(40000, "param_error", "参数错误"),
    OCR_SERVICE_ERROR(50000, "service_error", "ocr服务异常"),
    ;
    private final int code;

    private final String key;
    private final String desc;

    ResultCodeEnum(int code, String key, String desc) {
        this.code = code;
        this.desc = desc;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
