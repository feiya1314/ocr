package cn.easyocr.uni.auth.enums;

/**
 * @author : feiya
 * @date : 2023/6/24
 * @description :
 */
public enum Gender {
    UNKOWN("0", "未知"),

    MALE("1", "男"),

    FEMALE("2", "女"),
    ;
    private final String gender;
    private final String desc;

    Gender(String gender, String desc) {
        this.gender = gender;
        this.desc = desc;
    }

    public String getGender() {
        return gender;
    }

    public String getDesc() {
        return desc;
    }
}
