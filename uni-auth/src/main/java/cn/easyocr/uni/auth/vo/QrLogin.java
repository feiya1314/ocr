package cn.easyocr.uni.auth.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/8/13
 * @description :
 */
@Getter
@Setter
public class QrLogin {
    private String base64Qr;
    private String tempKey;
}
