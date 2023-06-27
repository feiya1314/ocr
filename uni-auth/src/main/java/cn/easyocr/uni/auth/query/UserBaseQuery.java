package cn.easyocr.uni.auth.query;

import lombok.Builder;
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
@Builder
public class UserBaseQuery {
    private Long id;
    private Long userId;
    private String mobile;
    private String email;
    private String username;
    private String password;
    private Integer ptd;
}
