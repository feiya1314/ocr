package cn.easyocr.uni.auth.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/8/13
 * @description :
 */
@Getter
@Setter
public class LoginCallBack {
    private String tempKey;
    /**
     * {@link LoginStatus.Status}
     */
    private String status;
}
