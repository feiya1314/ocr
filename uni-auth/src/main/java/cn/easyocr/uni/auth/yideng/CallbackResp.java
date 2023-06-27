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
public class CallbackResp {
    /**
     * 0:登录成功；其他：登录失败
     */
    private String code = "0";
    /**
     * 响应给用户的提示信息，该信息用于在小程序端进行提示
     */
    private String msg;
}
