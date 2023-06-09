package cn.easyocr.ai.chat.service.helper;

import cn.easyocr.ai.chat.service.config.OkHttpConfig;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.springframework.beans.factory.annotation.Autowired;
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
    public OkHttpClient client;

    private EventSource.Factory eventSourceFactory;

    @Autowired
    private OkHttpConfig okHttpConfig;

    @PostConstruct
    public void init() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(okHttpConfig.getConnectTimeout(), TimeUnit.SECONDS)
                .writeTimeout(okHttpConfig.getWriteTimeout(), TimeUnit.SECONDS)
                .readTimeout(okHttpConfig.getReadTimeout(), TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(okHttpConfig.getMaxIdle(), okHttpConfig.getKeepAliveDuration(), TimeUnit.MINUTES));
        client = clientBuilder.build();
        client.dispatcher().setMaxRequests(okHttpConfig.getMaxRequests());
        client.dispatcher().setMaxRequestsPerHost(okHttpConfig.getMaxRequestsPerHost());

        eventSourceFactory = EventSources.createFactory(client);
    }
    public EventSource.Factory eventSourceFactory() {
        return eventSourceFactory;
    }
}
