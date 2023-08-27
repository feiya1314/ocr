package cn.easyocr.uni.auth.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/8/5
 * @description :<a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html">...</a>
 */
@Getter
@Setter
public class GetQrResp extends BaseWxResp {
    private byte[] buffer;
}
