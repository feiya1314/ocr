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
public class GetTokenResp extends BaseWxResp{
    private String access_token;
    /**
     * 凭证有效时间，单位：秒。目前是7200秒之内的值
     */
    private Integer expires_in;
}
