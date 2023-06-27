package cn.easyocr.uni.auth.yideng;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/6/18
 * @description :
 */
@Getter
@Setter
public class CallbackReq {
    /**
     * true,扫码成功；false,扫码失败
     */
    private Boolean scanSuccess;
    /**
     * true,用户拒绝授权，登录不成功
     */
    private Boolean cancelLogin;
    /**
     * 用户临时标识
     */
    private String tempUserId;
    /**
     * wx 用户信息
     */
    private WxMaUserInfo wxMaUserInfo;

    @Getter
    @Setter
    public static class WxMaUserInfo {
        private String openId;
        private String nickName;
        private String gender;
        private String avatarUrl;
    }
}
