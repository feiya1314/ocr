package cn.easyocr.uni.auth.controller;

import cn.easyocr.common.enums.ResultCodeEnum;
import cn.easyocr.common.exception.AuthException;
import cn.easyocr.common.utils.TimeUtil;
import cn.easyocr.db.common.dao.annotation.ReqLogAnno;
import cn.easyocr.uni.auth.config.TokenConfig;
import cn.easyocr.uni.auth.config.WxConfig;
import cn.easyocr.uni.auth.enums.LoginPlatform;
import cn.easyocr.uni.auth.helper.LoginStatusHelper;
import cn.easyocr.uni.auth.mapper.UserBaseMapper;
import cn.easyocr.uni.auth.mapper.UserThirdPartyMapper;
import cn.easyocr.uni.auth.po.UserBase;
import cn.easyocr.uni.auth.po.UserThirdParty;
import cn.easyocr.uni.auth.query.UserThirdPartyQuery;
import cn.easyocr.uni.auth.util.ImageUtil;
import cn.easyocr.uni.auth.util.JwtUtil;
import cn.easyocr.uni.auth.util.SnowFlakeUidCreator;
import cn.easyocr.uni.auth.vo.QrLogin;
import cn.easyocr.uni.auth.wx.GetQrReq;
import cn.easyocr.uni.auth.wx.GetQrResp;
import cn.easyocr.uni.auth.wx.GetSessionReq;
import cn.easyocr.uni.auth.wx.GetSessionResp;
import cn.easyocr.uni.auth.wx.GetTokenReq;
import cn.easyocr.uni.auth.wx.GetTokenResp;
import cn.easyocr.uni.auth.wx.IWxService;
import cn.easyocr.uni.auth.wx.LoginCallBack;
import cn.easyocr.uni.auth.wx.LoginStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2023/8/6
 * @description :
 */
@Controller
@RequestMapping("/auth/v1/wx")
@Slf4j
public class WxAuthController {
    @Autowired
    private IWxService wxService;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private TokenConfig tokenConfig;

    @Autowired
    private LoginStatusHelper loginStatusHelper;

    @Autowired
    private UserThirdPartyMapper userThirdPartyMapper;

    @Autowired
    private SnowFlakeUidCreator snowFlakeUidCreator;

    @Autowired
    private UserBaseMapper userBaseMapper;

    /**
     * 获取小程序token
     */
    @GetMapping(value = "/accessToken", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public GetTokenResp getAccessToken() {
        GetTokenReq req = new GetTokenReq();
        req.setAppid(wxConfig.getAppid());
        req.setSecret(wxConfig.getSecret());

        return wxService.getAccessToken(req);
    }

    /**
     * 用户在页面获取小程序二维码
     */
    @GetMapping(value = "/getQr", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ReqLogAnno(origin = "wx-getQr")
    public ResponseEntity<QrLogin> getQr() throws Exception {
        String tempKey = loginStatusHelper.genTempKey();

        GetTokenReq tokenReq = new GetTokenReq();
        tokenReq.setAppid(wxConfig.getAppid());
        tokenReq.setSecret(wxConfig.getSecret());

        GetTokenResp tokenResp = wxService.getAccessToken(tokenReq);
        if (!tokenResp.success()) {
            throw new AuthException(ResultCodeEnum.WX_QR_ERROR);
        }

        GetQrReq req = new GetQrReq();
        req.setScene("tempKey=" + tempKey);
        req.setAccess_token(tokenResp.getAccess_token());
        req.setPage("pages/auth/auth");
        req.setEnv_version(wxConfig.getEnvVersion());

        GetQrResp resp = wxService.getUnlimitedQRCode(req);
        if (!resp.success()) {
            throw new AuthException(ResultCodeEnum.WX_QR_ERROR);
        }

        loginStatusHelper.updateLoginStatus(tempKey, LoginStatus.Status.GEN_QR);

        QrLogin qrLogin = new QrLogin();
        qrLogin.setTempKey(tempKey);
        qrLogin.setBase64Qr(ImageUtil.compressImage(resp.getBuffer(), 350, 350));

        return ResponseEntity.ok().body(qrLogin);
    }

    /**
     * 调用微信code2Session接口，进行登录认证，如果登录成功，则发放token
     *
     * @param code    小程序获取code
     * @param tempKey 后端生成的用户临时key
     */
    @GetMapping(value = "/code2Session", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<LoginStatus> code2Session(@RequestParam("code") String code, @RequestParam("tempKey") String tempKey) {
        log.info("code2Session code:{},tempKey:{}", code, tempKey);
        LoginStatus loginStatus = loginStatusHelper.getLoginStatus(tempKey);
        if (loginStatus == null) {
            throw new AuthException(ResultCodeEnum.AUTH_FAILED);
        }

        GetSessionReq req = new GetSessionReq();
        req.setAppid(wxConfig.getAppid());
        req.setSecret(wxConfig.getSecret());
        req.setJs_code(code);

        GetSessionResp sessionResp = wxService.code2Session(req);
        if (!sessionResp.success()) {
            loginStatusHelper.updateLoginStatus(tempKey, LoginStatus.Status.EXCEPTION);
            throw new AuthException(ResultCodeEnum.WX_SESSION_ERROR);
        }

        Long userId = getUserId(sessionResp.getUnionid(), sessionResp.getOpenid());
        loginStatus.setUserId(userId);

        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userId);

        String token = JwtUtil.genToken(claims, tokenConfig.getGenTokenSecret(), tokenConfig.getTokenExpiration());
        loginStatusHelper.updateLoginStatus(tempKey, LoginStatus.Status.LOGIN, token);
        log.info("code2Session finsh code:{},tempKey:{}", code, tempKey);
        return ResponseEntity.ok().body(loginStatus);
    }

    private Long getUserId(String unionid, String openid) {
        UserThirdPartyQuery userThirdPartyQuery = UserThirdPartyQuery.builder()
                .platform(LoginPlatform.WX_MINI.getPlatform())
                .openId(openid)
                .unionId(unionid)
                .build();

        List<UserThirdParty> userThirdParties = userThirdPartyMapper.find(userThirdPartyQuery);
        if (!CollectionUtils.isEmpty(userThirdParties)) {
            return userThirdParties.get(0).getUserId();
        }

        UserBase userBase = new UserBase();
        Long userId = snowFlakeUidCreator.genUid();
        userBase.setUserId(userId);
        userBase.setPtd(TimeUtil.getPtd());

        userBaseMapper.insert(userBase);

        UserThirdParty userThirdParty = new UserThirdParty();
        userThirdParty.setUserId(userId);
        userThirdParty.setOpenId(openid);
        userThirdParty.setUnionId(unionid);
        userThirdParty.setPlatform(LoginPlatform.WX_MINI.getPlatform());
        userThirdParty.setPtd(TimeUtil.getPtd());

        userThirdPartyMapper.insert(userThirdParty);

        return userId;
    }

    /**
     * 小程序登录状态回调
     */
    @PostMapping(value = "/loginCallback", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<LoginStatus> loginCallback(@RequestBody LoginCallBack loginCallBack) {
        log.info("loginCallback status:{},tempKey:{}", loginCallBack.getStatus(), loginCallBack.getTempKey());
        // todo 校验签名
        LoginStatus loginStatus = loginStatusHelper.getLoginStatus(loginCallBack.getTempKey());
        if (loginStatus == null) {
            throw new AuthException(ResultCodeEnum.AUTH_FAILED);
        }

        LoginStatus.Status status = LoginStatus.Status.valueOf(loginCallBack.getStatus());
        loginStatusHelper.updateLoginStatus(loginCallBack.getTempKey(), status);
        log.info("loginCallback finish status:{},tempKey:{}", loginCallBack.getStatus(), loginCallBack.getTempKey());

        return ResponseEntity.ok().body(loginStatusHelper.getLoginStatus(loginCallBack.getTempKey()));
    }

    /**
     * web页面轮询登录状态
     */
    @GetMapping(value = "/loginStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<LoginStatus> loginStatus(@RequestParam("tempKey") String tempKey) {
        log.info("loginStatus tempKey:{}", tempKey);
        LoginStatus loginStatus = loginStatusHelper.getLoginStatus(tempKey);
        if (loginStatus == null) {
            throw new AuthException(ResultCodeEnum.AUTH_FAILED);
        }

        if (LoginStatus.Status.finish(loginStatus.getStatus())) {
            loginStatusHelper.removeLoginStatus(tempKey);
        }
        log.info("loginStatus  finish tempKey:{}", tempKey);
        return ResponseEntity.ok().body(loginStatus);
    }

    /**
     * 用户手机号信息
     */
    public void userAuthorize() {

    }
}
