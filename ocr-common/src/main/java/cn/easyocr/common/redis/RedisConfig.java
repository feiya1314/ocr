package cn.easyocr.common.redis;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @description :
 * @since : 2023/2/5
 */
@Getter
@Setter
public class RedisConfig {
    private String host;
    private Integer port;
    private String pwd;
    private Integer maxTotal;
    private Integer maxIdle;
    private Boolean testOnBorrow;
    private Long maxWaitMills;
}
