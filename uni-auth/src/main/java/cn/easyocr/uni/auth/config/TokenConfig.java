package cn.easyocr.uni.auth.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/8/13
 * @description :
 */
@Getter
@Setter
public class TokenConfig {
    private String genTokenSecret;
    private long tokenExpiration = 86400000L;
}
