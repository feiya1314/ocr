package cn.easyocr.uni.auth.wx;

import cn.easyocr.common.helper.HttpClientHelper;
import cn.easyocr.common.helper.HttpEntityCopy;
import cn.easyocr.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2023/8/5
 * @description :
 */
@Slf4j
public class WxService implements IWxService {
    private final HttpClientHelper httpClientHelper;
    private final String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
    private final String stableTokenUrl = "https://api.weixin.qq.com/cgi-bin/stable_token";
    private final String qrUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit";
    private final String jscode2sessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
    private long tokenExpireTime;
    private GetTokenResp resp;

    public WxService(HttpClientHelper httpClientHelper) {
        this.httpClientHelper = httpClientHelper;
    }

    @Override
    public GetTokenResp getAccessToken(GetTokenReq req) {
        if (resp != null && System.currentTimeMillis() < tokenExpireTime) {
            return resp;
        }

        synchronized (this) {
            GetTokenResp tokenResp = reqNewAccessToken(req);
            resp = tokenResp;
            int expiration = tokenResp.getExpires_in() == null ? 0 : tokenResp.getExpires_in() - 30;
            tokenExpireTime = System.currentTimeMillis() + (expiration * 1000L);
        }

        return resp;
    }

    @Override
    public GetQrResp getUnlimitedQRCode(GetQrReq req) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", req.getAccess_token());
        req.setAccess_token(null);

        HttpEntityCopy response = httpClientHelper.doPostJsonBytes(qrUrl, params, JsonUtils.toJson(req));
        if (response.getContentType() != null && response.getContentType().startsWith("image")) {
            GetQrResp resp = new GetQrResp();
            resp.setBuffer(response.getData());

            return resp;
        }

        return JsonUtils.jsonToBean(new String(response.getData()), GetQrResp.class);
    }

    @Override
    public GetSessionResp code2Session(GetSessionReq req) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", req.getAppid());
        params.put("secret", req.getSecret());
        params.put("js_code", req.getJs_code());
        params.put("grant_type", req.getGrant_type());

        String resp = httpClientHelper.doGet(jscode2sessionUrl, params);
        return JsonUtils.jsonToBean(resp, GetSessionResp.class);
    }

    private GetTokenResp reqNewAccessToken(GetTokenReq req) {
        String resp = httpClientHelper.doPostJson(stableTokenUrl, JsonUtils.toJson(req));
        // todo del log
        log.info("token result:{}", resp);
        return JsonUtils.jsonToBean(resp, GetTokenResp.class);
    }
}
