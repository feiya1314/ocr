package cn.easyocr.ai.chat.service.enums;

/**
 * @author : feiya
 * @date : 2023/5/18
 * @description :
 */
public enum ChatRole {
    USER("user", 1, "用户"),

    ASSISTANT("assistant", 2, "ai"),

    SYSTEM("system", 3, "系统"),
    ;

    private final String role;
    private final int roleId;
    private final String desc;

    ChatRole(String role, int roleId, String desc) {
        this.role = role;
        this.roleId = roleId;
        this.desc = desc;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRole() {
        return role;
    }

    public String getDesc() {
        return desc;
    }
}
