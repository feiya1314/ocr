package cn.easyocr.uni.auth.enums;

/**
 * @author : feiya
 * @date : 2023/6/24
 * @description :
 */
public enum LoginPlatform {
    LOCAL(1, "本地登录系统"),

    YD(2, "易登系统"),

    WX_MINI(3, "微信小程序"),
    ;
    private final int platform;
    private final String desc;

    LoginPlatform(int platform, String desc) {
        this.platform = platform;
        this.desc = desc;
    }

    public int getPlatform() {
        return platform;
    }

    public String getDesc() {
        return desc;
    }
}
