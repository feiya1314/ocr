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

    public void updateLoginStatus(String key, LoginStatus.Status status,String token) {
        tempKeys.computeIfPresent(key, (k, v) -> {
            v.setStatus(status);
            v.setUpdateTime(System.currentTimeMillis());
            v.setToken(token);
            return v;
        });
    }

    public void updateLoginStatus(String key, LoginStatus.Status status) {
        tempKeys.computeIfPresent(key, (k, v) -> {
            v.setStatus(status);
            v.setUpdateTime(System.currentTimeMillis());
            return v;
        });
    }

    public String genTempKey() {
        return UuidUtil.gen9Uuid();
    }
}
