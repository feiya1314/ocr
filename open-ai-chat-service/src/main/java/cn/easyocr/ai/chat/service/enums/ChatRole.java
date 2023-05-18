package cn.easyocr.ai.chat.service.enums;

/**
 * @author : feiya
 * @date : 2023/5/18
 * @description :
 */
public enum ChatRole {
    USER("user", "用户"),

    ASSISTANT("assistant", "ai"),

    SYSTEM("system", "系统"),
    ;

    private final String role;
    private final String desc;

    ChatRole(String role, String desc) {
        this.role = role;
        this.desc = desc;
    }

    public String getRole() {
        return role;
    }

    public String getDesc() {
        return desc;
    }
}
