package cn.easy.ocr.main.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : feiya
 * @description :
 * @since : 2023/2/5
 */
@Component
@ConfigurationProperties(prefix = "redis")
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
