package cn.easyocr.uni.auth.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/8/5
 * @description : <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getAccessToken.html">...</a>
 */
@Getter
@Setter
public class GetTokenReq {
    private String grant_type = "client_credential";
    private String appid;
    private String secret;
    private Boolean force_refresh;
}
