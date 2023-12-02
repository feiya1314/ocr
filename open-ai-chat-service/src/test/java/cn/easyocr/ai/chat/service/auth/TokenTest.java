package cn.easyocr.ai.chat.service.auth;

import cn.easyocr.ai.chat.service.BaseTest;
import cn.easyocr.common.utils.JsonUtils;
import cn.easyocr.uni.auth.config.TokenConfig;
import cn.easyocr.uni.auth.util.JwtUtil;
import cn.easyocr.uni.auth.wx.LoginStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2023/10/29
 * @description :
 */
public class TokenTest extends BaseTest {
    @Autowired
    private TokenConfig tokenConfig;

    @Test
    public void genToken() throws Exception {
//        SnowFlakeUidCreator snowFlakeUidCreator = new SnowFlakeUidCreator(0, 0);
//        Long userId = snowFlakeUidCreator.genUid();

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtUtil.USER_ID, 9527L);

        long expiredTime = System.currentTimeMillis() + tokenConfig.getTokenExpiration();
        String token = JwtUtil.genToken(claims, tokenConfig.getGenTokenSecret(), expiredTime);

        LoginStatus loginStatus = new LoginStatus();
        loginStatus.setToken(token);
        loginStatus.setTokenExpiredTime(expiredTime);

        System.out.println(JsonUtils.toJson(loginStatus));
    }
}
