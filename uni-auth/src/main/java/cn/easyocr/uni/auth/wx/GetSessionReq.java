package cn.easyocr.uni.auth.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/8/6
 * @description : <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html">...</a>
 */
@Getter
@Setter
public class GetSessionReq {
    private String appid;
    private String secret;
    private String js_code;
    private String grant_type="authorization_code";
}
