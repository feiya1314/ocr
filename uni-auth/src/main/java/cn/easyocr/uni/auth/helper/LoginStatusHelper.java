package cn.easyocr.uni.auth.helper;

import cn.easyocr.common.utils.UuidUtil;
import cn.easyocr.uni.auth.wx.LoginStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : feiya
 * @date : 2023/8/9
 * @description :
 */
public class LoginStatusHelper {
    private final Map<String, LoginStatus> tempKeys = new ConcurrentHashMap<>();

    public void initLoginStatus(String key) {
        LoginStatus loginStatus = new LoginStatus();
        loginStatus.setStatus(LoginStatus.Status.INIT);
        loginStatus.setUpdateTime(System.currentTimeMillis());
        tempKeys.put(key, loginStatus);
    }

    public LoginStatus getLoginStatus(String key) {
        return tempKeys.get(key);
    }

    public void removeLoginStatus(String key) {
        tempKeys.remove(key);
    }

    public void updateLoginStatus(String key, LoginStatus.Status status, String token) {
        tempKeys.compute(key, (k, v) -> {
            LoginStatus tmp = v == null ? new LoginStatus() : v;
            tmp.setStatus(status);
            tmp.setUpdateTime(System.currentTimeMillis());
            tmp.setToken(token);

            return tmp;
        });
    }

    public void updateLoginStatus(String key, LoginStatus.Status status) {
        tempKeys.compute(key, (k, v) -> {
            LoginStatus tmp = v == null ? new LoginStatus() : v;
            tmp.setStatus(status);
            tmp.setUpdateTime(System.currentTimeMillis());
            return tmp;
        });
    }

    public String genTempKey() {
        return UuidUtil.gen9Uuid();
    }
}
