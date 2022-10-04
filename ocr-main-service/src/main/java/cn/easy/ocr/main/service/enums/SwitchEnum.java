package cn.easy.ocr.main.service.enums;

/**
 * @author : feiya
 * @date : 2022/10/4
 * @description :
 */
public enum SwitchEnum {
    ON(1, true, "开启"),
    OFF(0, false, "关闭"),
    ;
    private final int type;
    private final boolean enable;
    private final String desc;

    SwitchEnum(int type, boolean enable, String desc) {
        this.type = type;
        this.enable = enable;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getDesc() {
        return desc;
    }
}
