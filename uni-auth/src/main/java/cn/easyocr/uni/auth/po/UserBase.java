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
public class UserBase {
    private Long id;
    private Long userId;
    private String mobile;
    private String email;
    private String nickName;
    private String username;
    private String password;
    /**
     * {@link cn.easyocr.uni.auth.enums.Gender}
     */
    private String gender;
    private String avatar;
    private Date createdTime;
    private Date updateTime;
    private Integer ptd;
}
