package cn.easyocr.ai.chat.service.config;

import cn.easyocr.common.helper.HttpClientHelper;
import cn.easyocr.common.thread.RequestLogThreadPool;
import cn.easyocr.uni.auth.config.TokenConfig;
import cn.easyocr.uni.auth.config.WxConfig;
import cn.easyocr.uni.auth.helper.LoginStatusHelper;
import cn.easyocr.uni.auth.util.SnowFlakeUidCreator;
import cn.easyocr.uni.auth.wx.IWxService;
import cn.easyocr.uni.auth.wx.WxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author : feiya
 * @description :
 * @since : 2023/4/9
 */
@Configuration
// 使用@ComponentScan额外指定待扫描的包，但是不能用在主启动类上，因为这样会覆盖掉默认的包扫描规则
@ComponentScan(basePackages = {"cn.easyocr.uni.auth.controller"})
public class BeansConfig {
    @Autowired
    private AuthConfig authConfig;

    @Bean
    public RequestLogThreadPool requestLogThreadPool() {
        return new RequestLogThreadPool();
    }

    @Bean
    public SnowFlakeUidCreator snowFlakeUidCreator() {
        return new SnowFlakeUidCreator(0, 0);
    }

    @Bean
    public LoginStatusHelper loginStatusHelper() {
        return new LoginStatusHelper();
    }

    @Bean
    public HttpClientHelper httpClientHelper() {
        return new HttpClientHelper();
    }

    @Bean
    public IWxService wxService(HttpClientHelper httpClientHelper) {
        return new WxService(httpClientHelper);
    }

    @Bean
    public WxConfig wxConfig() {
        WxConfig wxConfig = new WxConfig();
        wxConfig.setAppid(authConfig.getWxConfig().getAppid());
        wxConfig.setSecret(authConfig.getWxConfig().getSecret());

        return wxConfig;
    }

    @Bean
    public TokenConfig tokenConfig() {
        TokenConfig tokenConfig = new TokenConfig();
        tokenConfig.setGenTokenSecret(authConfig.getGenTokenSecret());
        tokenConfig.setTokenExpiration(authConfig.getTokenExpiration());

        return tokenConfig;
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
