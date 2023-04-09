package cn.easy.ocr.main.service.config;

import cn.easy.ocr.main.service.interceptor.RequestLogInterceptor;
import cn.easyocr.common.interceptor.RequestTraceInterceptor;
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
    private RequestLogInterceptor requestLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 加入的顺序就是拦截器执行的顺序
        registry.addInterceptor(new RequestTraceInterceptor());
        registry.addInterceptor(requestLogInterceptor);
    }
}
