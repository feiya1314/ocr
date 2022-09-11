package cn.easy.ocr.main.service.config;

import cn.easy.ocr.main.service.interceptor.RequestTraceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : feiya
 * @date : 2022/9/11
 * @description :
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private RequestTraceInterceptor requestTraceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestTraceInterceptor);
    }
}
