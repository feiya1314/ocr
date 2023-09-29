package cn.easyocr.uni.auth.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/6/23
 * @description :
 */
@Getter
@Setter
public class LoginInfo {
    private Long userId;
    private String username;
    private String avatarUrl;
    private String token;
    private Long tokenExpiredTime;
}
