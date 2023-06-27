package cn.easyocr.uni.auth.yideng;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/6/20
 * @description :
 */
@Getter
@Setter
public class TempUserResp {
    private Integer code;
    private String msg;
    private UserInfo data;

    public boolean success() {
        return code != null && code == 0;
    }

    @Getter
    @Setter
    public static class UserInfo {
        private String qrUrl;
        private String tempUserId;
    }
}
