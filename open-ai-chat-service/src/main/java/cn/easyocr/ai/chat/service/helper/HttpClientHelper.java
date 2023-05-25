package cn.easyocr.ai.chat.service.helper;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author : feiya
 * @date : 2023/5/25
 * @description :
 */
@Component
public class HttpClientHelper {
    private OkHttpClient client;

    private EventSource.Factory eventSourceFactory;

    private int readTimeout = 60;
    private int connectTimeout = 5;
    private int writeTimeout = 60;
    /**
     * 最大空闲连接数， 如果一个连接正在发送请求，那它就是活跃的，否则它就是空闲的
     */
    private int maxIdle = 10;

    /**
     * 最大空闲连接时间
     */
    private int keepAliveDuration = 5;

    /**
     * 当前okhttpclient实例最大的并发请求数
     * 设置整个 OkHttp 客户端实例的最大请求数量。它限制了同时进行的请求数量，无论是针对同一主机还是多个主机。当达到 maxRequests 的限制时，后续的请求将会被排队等待执行。
     */
    private int maxRequests = 64;

    /**
     * 单个主机最大请求并发数，这里的主机指被请求方主机，一般可以理解对调用方有限流作用。注意：websocket请求不受这个限制。
     * maxRequests和maxRequestPerHost是okhttp内部维持的请求队列，而executorservice是实际发送请求的线程。如果maxRequests和maxReuestPerHost设置太大，
     * executorService会因为线程太少而阻塞发送。
     */
    private int maxRequestsPerHost = 4;

    @PostConstruct
    public void init() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
//                .eventListenerFactory()
                .connectionPool(new ConnectionPool(maxIdle, keepAliveDuration, TimeUnit.MINUTES));
        client = clientBuilder.build();
        // client.dispatcher().setMaxRequests();
        // client.dispatcher().setMaxRequestsPerHost(maxRequestsPerHost);

        eventSourceFactory = EventSources.createFactory(client);
    }

    public OkHttpClient client() {
        return client;
    }

    public EventSource.Factory eventSourceFactory() {
        return eventSourceFactory;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }
}
