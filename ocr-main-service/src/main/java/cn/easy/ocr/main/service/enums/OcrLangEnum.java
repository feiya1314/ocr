package cn.easy.ocr.main.service.enums;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
public enum OcrLangEnum {
    CH("ch", "ch", "中文", "中文"),
    EN("en", "en", "英文", "英文"),
    JAPAN("japan", "japan", "日语", "日语"),
    FRENCH("french", "french", "法语", "法语"),
    GERMAN("german", "german", "德语", "德语"),
    KOREAN("korean", "korean", "韩文", "韩文"),

    RUSSIA("russia", "russia", "俄语", "俄语"),
    ;
    private final String code;
    private final String lang;
    private final String display;
    private final String description;

    OcrLangEnum(String code, String lang, String display, String description) {
        this.code = code;
        this.lang = lang;
        this.description = description;
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public String getCode() {
        return code;
    }

    public String getLang() {
        return lang;
    }

    public String getDescription() {
        return description;
    }
}
