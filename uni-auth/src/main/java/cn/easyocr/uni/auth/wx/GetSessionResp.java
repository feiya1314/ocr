package cn.easyocr.uni.auth.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/8/6
 * @description :
 */
@Getter
@Setter
public class GetSessionResp extends BaseWxResp{
    private String session_key;
    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台账号下会返回，详见 UnionID 机制说明。
     * <a href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html">...</a>
     */
    private String unionid;
    private String openid;
}
