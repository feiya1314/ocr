package cn.easyocr.uni.auth.wx;

import cn.easyocr.uni.auth.vo.UserInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/8/9
 * @description :
 */
@Getter
@Setter
public class LoginStatus extends UserInfo {
    private Status status;
    private String msg;
    private Long updateTime;

    public enum Status {
        /**
         * 初始化
         */
        INIT,
        /**
         * 已生成二维码
         */
        GEN_QR,
        /**
         * 小程序已扫码二维码,等待授权
         */
        SCAN_QR,
        /**
         * 小程序登录中
         */
        LOGINING,
        /**
         * 小程序已授权登录
         */
        LOGIN,
        /**
         * 小程序拒绝登录
         */
        REFUSE,
        /**
         * 登录流程异常
         */
        EXCEPTION;

        public static boolean finish(Status status) {
            return status == LOGIN || status == REFUSE || status == EXCEPTION;
        }
    }
}
