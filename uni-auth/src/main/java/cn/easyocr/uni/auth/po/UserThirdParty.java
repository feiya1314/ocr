package cn.easyocr.uni.auth.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author : feiya
 * @date : 2023/6/24
 * @description :
 */
@Getter
@Setter
public class UserThirdParty {
    private Long id;
    private Long userId;
    private String unionId;
    private String openId;
    private String privilege;
    /**
     * {@link cn.easyocr.uni.auth.enums.LoginPlatform}
     */
    private Integer platform;
    private String appId;
    private Date createdTime;
    private Date updateTime;
    private Integer ptd;
}
