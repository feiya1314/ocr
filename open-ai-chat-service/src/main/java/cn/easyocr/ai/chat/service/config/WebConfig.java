package cn.easyocr.ai.chat.service.config;

import cn.easyocr.common.interceptor.RequestTraceInterceptor;
import cn.easyocr.common.thread.RequestLogThreadPool;
import cn.easyocr.db.common.dao.interceptor.RequestLogInterceptor;
import cn.easyocr.db.common.dao.mapper.OcrRequestLogMapper;
import cn.easyocr.uni.auth.interceptor.AuthInterceptor;
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
    private AuthConfig authConfig;

    @Autowired
    private OcrRequestLogMapper requestLogMapper;

    @Autowired
    private RequestLogThreadPool threadPool;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 加入的顺序就是拦截器执行的顺序, 添加的路径不包含项目的 ontext-path: /chat
        registry.addInterceptor(new RequestTraceInterceptor())
                .addPathPatterns("/api/**")
                .addPathPatterns("/auth/**");

        registry.addInterceptor(new RequestLogInterceptor(requestLogMapper, threadPool))
                .addPathPatterns("/api/**")
                .addPathPatterns("/auth/**");

        registry.addInterceptor(new AuthInterceptor(uri -> uri.equals("/") || uri.startsWith("/assets")
                        || uri.startsWith("/favicon.svg") || uri.startsWith("/chat/auth/v1"),
                        authConfig.getGenTokenSecret()))
                .addPathPatterns("/**");
    }
}
