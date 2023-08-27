package cn.easyocr.uni.auth.wx;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/8/10
 * @description :
 */
@Getter
@Setter
public class BaseWxResp {
    private Integer errcode;
    private String errmsg;

    public boolean success() {
        return errcode == null || errcode == 0;
    }
}
