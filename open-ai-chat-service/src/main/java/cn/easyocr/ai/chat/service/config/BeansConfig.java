package cn.easyocr.ai.chat.service.config;

import cn.easyocr.common.thread.RequestLogThreadPool;
import cn.easyocr.uni.auth.util.SnowFlakeUidCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : feiya
 * @description :
 * @since : 2023/4/9
 */
@Configuration
public class BeansConfig {
    @Autowired
    private ChatConfig config;

    @Bean
    public RequestLogThreadPool requestLogThreadPool() {
        return new RequestLogThreadPool();
    }

    @Bean
    public SnowFlakeUidCreator snowFlakeUidCreator() {
        return new SnowFlakeUidCreator(0, 0);
    }

//    @Bean(name = "authFilter")
//    public FilterRegistrationBean<AuthFilter> authFilter() {
//        AuthFilter myFiler = new AuthFilter(uri -> uri.equals("/") || uri.startsWith("/assets") || uri.startsWith("/favicon.svg"),
//                config.getGenTokenSecret());
//        FilterRegistrationBean<AuthFilter> filterRegistrationBean = new FilterRegistrationBean<>(myFiler);
//        //注册该过滤器需要过滤的 url
//        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
//        filterRegistrationBean.setOrder(1);
//        return filterRegistrationBean;
//    }

//    @Bean
//    public JedisManager jedisManager() {
//        JedisManager jedisManager = new JedisManager(redisConfig());
//        jedisManager.init();
//        return jedisManager;
//    }
//
//    @Bean
//    public RedissonManager redissonManager() {
//        RedissonManager redissonManager = new RedissonManager(redisConfig());
//        redissonManager.init();
//        return redissonManager;
//    }
//
//    @ConfigurationProperties(prefix = "redis")
//    @Bean
//    public RedisConfig redisConfig() {
//        return new RedisConfig();
//    }
}
