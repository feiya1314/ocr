package cn.easyocr.uni.auth.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/6/24
 * @description :
 */
@Getter
@Setter
@Builder
public class UserThirdPartyQuery {
    private Long id;
    private Long userId;
    private String unionId;
    private String openId;
    private Integer platform;
    private String appId;
    private Integer ptd;
}
