package cn.easyocr.ai.chat.service.controller;

import cn.easyocr.ai.chat.service.config.ChatConfig;
import cn.easyocr.ai.chat.service.helper.HttpClientHelper;
import cn.easyocr.common.enums.ResultCodeEnum;
import cn.easyocr.common.exception.AuthException;
import cn.easyocr.common.exception.ParamValidateException;
import cn.easyocr.common.exception.ServiceException;
import cn.easyocr.common.utils.JsonUtils;
import cn.easyocr.common.utils.TimeUtil;
import cn.easyocr.uni.auth.enums.LoginPlatform;
import cn.easyocr.uni.auth.mapper.UserBaseMapper;
import cn.easyocr.uni.auth.mapper.UserThirdPartyMapper;
import cn.easyocr.uni.auth.po.UserBase;
import cn.easyocr.uni.auth.po.UserThirdParty;
import cn.easyocr.uni.auth.query.UserThirdPartyQuery;
import cn.easyocr.uni.auth.util.JwtUtil;
import cn.easyocr.uni.auth.util.SnowFlakeUidCreator;
import cn.easyocr.uni.auth.yideng.CallbackReq;
import cn.easyocr.uni.auth.yideng.CallbackResp;
import cn.easyocr.uni.auth.yideng.TempUserResp;
import cn.easyocr.uni.auth.yideng.UserInfo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author : feiya
 * @date : 2023/6/19
 * @description :
 */
@Controller
@RequestMapping("/auth/v1")
@Slf4j
public class AuthController {
    @Autowired
    private ChatConfig config;
    private final String tempUserIdUrl = "https://yd.jylt.cc/api/wxLogin/tempUserId";

    private Map<String, CallbackReq> userLoninCallbacks = new HashMap<>();
    @Autowired
    private HttpClientHelper httpHelper;

    @Autowired
    private UserThirdPartyMapper userThirdPartyMapper;

    @Autowired
    private UserBaseMapper userBaseMapper;

    @Autowired
    private SnowFlakeUidCreator snowFlakeUidCreator;

    @GetMapping(value = "/wxQr", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TempUserResp.UserInfo> wxQr() {
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(tempUserIdUrl)).newBuilder();
        builder.addQueryParameter("secret", config.getYdSecret());
        Request request = new Request.Builder().url(builder.build()).get().build();

        Call call = httpHelper.client.newCall(request);
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                log.error("get yf qr failed, status :{},msg:{}", response.code(), response.message());
                throw new ServiceException(ResultCodeEnum.YD_QR_ERROR);
            }
            okhttp3.ResponseBody body = response.body();
            if (body == null) {
                log.error("get yf qr body is null");
                throw new ServiceException(ResultCodeEnum.YD_QR_ERROR);
            }
            TempUserResp tempUserResp = JsonUtils.jsonToBean(body.string(), TempUserResp.class);
            if (tempUserResp == null) {
                log.error("get yf qr body is not avaliable ,body :{}", body.string());
                throw new ServiceException(ResultCodeEnum.YD_QR_ERROR);
            }
            if (!tempUserResp.success()) {
                log.error("get yf qr result not success msg:{}", tempUserResp.getMsg());
                throw new ServiceException(ResultCodeEnum.YD_QR_ERROR);
            }

            return ResponseEntity.ok(tempUserResp.getData());
        } catch (IOException e) {
            log.error("yf req error", e);
            throw new ServiceException(ResultCodeEnum.YD_REQ_ERROR);
        }
    }

    @PostMapping(value = "/yd/callback", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CallbackResp> callback(@RequestBody CallbackReq callbackReq) {
        userLoninCallbacks.put(callbackReq.getTempUserId(), callbackReq);
        CallbackResp resp = new CallbackResp();
        resp.setMsg("登录成功");
        return ResponseEntity.ok(resp);
    }

    @GetMapping(value = "/yd/userInfo/{tempId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserInfo> userInfo(@PathVariable("tempId") String tempId) {
        if (!userLoninCallbacks.containsKey(tempId)) {
            throw new ParamValidateException(ResultCodeEnum.AUTH_INFO_NOT_FOUND);
        }

        CallbackReq callbackReq = userLoninCallbacks.remove(tempId);
        if (callbackReq.getCancelLogin() != null && callbackReq.getCancelLogin()) {
            throw new AuthException(ResultCodeEnum.AUTH_CANCELLED);
        }

        if (callbackReq.getScanSuccess() != null && callbackReq.getScanSuccess()) {
            throw new AuthException(ResultCodeEnum.AUTH_IN_PROGRESS);
        }

        CallbackReq.WxMaUserInfo wxMaUserInfo = callbackReq.getWxMaUserInfo();

        Long userId = getUserId(wxMaUserInfo);
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userId);

        String token = JwtUtil.genToken(claims, config.getGenTokenSecret(), config.getTokenExpiration());

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUsername(wxMaUserInfo.getNickName());
        userInfo.setAvatarUrl(wxMaUserInfo.getAvatarUrl());
        userInfo.setToken(token);

        return ResponseEntity.ok(userInfo);
    }

    private Long getUserId(CallbackReq.WxMaUserInfo wxMaUserInfo) {
        String openId = wxMaUserInfo.getOpenId();
        UserThirdPartyQuery userThirdPartyQuery = UserThirdPartyQuery.builder()
                .platform(LoginPlatform.YD.getPlatform())
                .openId(openId)
                .build();
        List<UserThirdParty> userThirdParties = userThirdPartyMapper.find(userThirdPartyQuery);
        if (!CollectionUtils.isEmpty(userThirdParties)) {
            return userThirdParties.get(0).getUserId();
        }

        UserBase userBase = new UserBase();
        Long userId = snowFlakeUidCreator.genUid();
        userBase.setUserId(userId);
        userBase.setNickName(wxMaUserInfo.getNickName());
        userBase.setUsername(wxMaUserInfo.getNickName());

        userBase.setGender(wxMaUserInfo.getGender());
        userBase.setAvatar(wxMaUserInfo.getAvatarUrl());
        userBase.setPtd(TimeUtil.getPtd());

        userBaseMapper.insert(userBase);

        UserThirdParty userThirdParty = new UserThirdParty();
        userThirdParty.setUserId(userId);
        userThirdParty.setOpenId(openId);
        userThirdParty.setPlatform(LoginPlatform.YD.getPlatform());
        userThirdParty.setPtd(TimeUtil.getPtd());

        userThirdPartyMapper.insert(userThirdParty);

        return userId;
    }
}
