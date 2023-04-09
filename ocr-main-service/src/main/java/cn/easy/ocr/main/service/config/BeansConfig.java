package cn.easy.ocr.main.service.config;

import cn.easyocr.common.thread.RequestLogThreadPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : feiya
 * @description :
 * @since : 2023/4/9
 */
@Configuration
public class BeansConfig {
    @Bean
    public RequestLogThreadPool requestLogThreadPool() {
        return new RequestLogThreadPool();
    }
}
