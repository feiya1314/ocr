package cn.easy.ocr.main.service.enums;

/**
 * @author : feiya
 * @date : 2022/9/6
 * @description : 百度的ocr 语言码
 */
public enum BdOcrLangEnum {
    CH("CHN_ENG", OcrLangEnum.CH),
    EN("ENG", OcrLangEnum.EN),
    JAPAN("JAP", OcrLangEnum.JAPAN),
    FRENCH("FRE", OcrLangEnum.FRENCH),
    GERMAN("GER", OcrLangEnum.GERMAN),
    KOREAN("KOR", OcrLangEnum.KOREAN),
    RUSSIA("RUS", OcrLangEnum.RUSSIA),
    ;
    private final String code;
    private final OcrLangEnum lang;

    BdOcrLangEnum(String code, OcrLangEnum lang) {
        this.code = code;
        this.lang = lang;
    }

    public String getCode() {
        return code;
    }

    public OcrLangEnum getLang() {
        return lang;
    }

    public static String getLangCode(OcrLangEnum lang) {
        for (BdOcrLangEnum e : values()) {
            if (e.getLang().equals(lang)) {
                return e.getCode();
            }
        }
        return CH.getCode();
    }
}
