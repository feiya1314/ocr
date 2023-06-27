package cn.easyocr.uni.auth.yideng;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/6/23
 * @description :
 */
@Getter
@Setter
public class UserInfo {
    private Long userId;
    private String username;
    private String avatarUrl;
    private String token;
}
