package cn.easyocr.uni.auth.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/8/6
 * @description : <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html">...</a>
 */
@Getter
@Setter
public class GetQrReq {
    private String access_token;
    private String scene;
    private String page;
    private Boolean check_path;
    private String env_version;
    private Integer width;
    private Boolean auto_color;
    private Object line_color;
    private Boolean is_hyaline;
}
