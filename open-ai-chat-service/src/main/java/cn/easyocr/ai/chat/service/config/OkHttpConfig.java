package cn.easyocr.ai.chat.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : feiya
 * @date : 2023/6/9
 * @description :
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "okhttp3")
@Component
public class OkHttpConfig {
    private int readTimeout = 60;

    private int connectTimeout = 5;

    private int writeTimeout = 60;

    /**
     * 最大空闲连接数， 如果一个连接正在发送请求，那它就是活跃的，否则它就是空闲的
     */
    private int maxIdle;

    /**
     * 最大空闲连接时间,单位分钟
     */
    private int keepAliveDuration = 3;

    /**
     * 当前okhttpclient实例最大的并发请求数
     * 设置整个 OkHttp 客户端实例的最大请求数量。它限制了同时进行的请求数量，无论是针对同一主机还是多个主机。当达到 maxRequests 的限制时，后续的请求将会被排队等待执行。
     */
    private int maxRequests;

    /**
     * 单个主机最大请求并发数，这里的主机指被请求方主机，一般可以理解对调用方有限流作用。注意：websocket请求不受这个限制。
     * maxRequests和maxRequestPerHost是okhttp内部维持的请求队列，而executorservice是实际发送请求的线程。如果maxRequests和maxReuestPerHost设置太大，
     * executorService会因为线程太少而阻塞发送。
     */
    private int maxRequestsPerHost;
}
