package cn.easyocr.uni.auth.wx;

/**
 * @author : feiya
 * @date : 2023/8/6
 * @description : 调用微信相关服务接口
 */
public interface IWxService {
    /**
     * 获取token，且方法线程安全，避免token重复请求，token有过期时间，超出过期时间时不重复请求
     *
     * @param req req
     * @return token
     */
    GetTokenResp getAccessToken(GetTokenReq req);

    /**
     * 从小程序获取二维码
     *
     * @param req 获取二维码请求
     * @return 二维码结果
     */
    GetQrResp getUnlimitedQRCode(GetQrReq req);

    /**
     * 使用小程序code获取微信登录状态
     *
     * @param req 登录请求
     * @return 微信小程序登录结果
     */
    GetSessionResp code2Session(GetSessionReq req);
}
