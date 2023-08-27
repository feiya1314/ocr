package cn.easyocr.common.helper;

import cn.easyocr.common.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2023/8/5
 * @description :
 */
@Slf4j
public class HttpClientHelper {
    /**
     * httpclient是一个线程安全的类
     */
    private final CloseableHttpClient httpClient;

    /**
     * 在使用HTTP连接池时，当所有的连接都被占用时，如果再有新的请求进来，就需要等待一段时间，
     * 看是否有连接被释放。这个等待的时间就是连接请求超时。如果等待超时，那么就会抛出连接请求超时异常
     */
    private int connReqTimeout = 3000;
    /**
     * // 设置连接超时时间 当尝试建立与远程服务器的连接时，如果在规定的时间内未能建立连接，就会抛出连接超时异常。
     * 这个超时设置通常是在客户端程序中设置的，可以防止程序长时间阻塞在连接建立的过程中
     */
    private int connTimeout = 3000;
    /**
     * 响应超时时间 在已经建立连接的情况下，当程序通过Socket发送请求时，如果在规定的时间内未能得到响应，
     * 就会抛出套接字超时异常。这个超时设置通常是在客户端和服务器端都可以设置的，可以避免程序长时间阻塞在等待响应的过程中。
     */
    private int socketTimeout = 3000;
    /**
     * 连接池最大连接数
     */
    private int maxTotal = 100;

    /**
     * 是设置一个 host(ip或域名):port 同时间正在使用的最大连接数
     * 设置针对某个 host:port 设置最大连接数
     */
    private int maxPerRoute = 30;
    /**
     * //接收数据的等待超时时间，单位ms
     */
    private int setSoTimeout = 500;
    /**
     * //关闭Socket时，要么发送完所有数据，要么等待60s后，就关闭连接，此时socket.close()是阻塞的
     */
    private int soLinger = 60;
    /**
     * //是否立即发送数据，设置为true会关闭Socket缓冲，默认为false
     */
    private boolean tcpNoDelay = false;
    /**
     * //开启监视TCP连接是否有效
     */
    private boolean soKeepAlive = true;
    /**
     * //是否可以在一个进程关闭Socket后，即使它还没有释放端口，其它进程还可以立即重用端口
     */
    private boolean soReuseAddress = true;

    public HttpClientHelper() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
        headers.add(new BasicHeader(HttpHeaders.ACCEPT, "*/*"));

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connReqTimeout)
                .setConnectTimeout(connTimeout)
                .setSocketTimeout(socketTimeout)
                .build();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(maxTotal);
        connManager.setDefaultMaxPerRoute(maxPerRoute);

        /**
         * socket配置（默认配置 和 某个host的配置）
         */
//        SocketConfig socketConfig = SocketConfig.custom()
//                .setTcpNoDelay(tcpNoDelay)
//                .setSoReuseAddress(soReuseAddress)
//                .setSoTimeout(setSoTimeout)
//                .setSoLinger(soLinger)
//                .setSoKeepAlive(soKeepAlive)
//                .build();
//        connManager.setDefaultSocketConfig(socketConfig);
//        connManager.setSocketConfig(new HttpHost("somehost", 80), socketConfig);
        //设置到某个路由的最大连接数，会覆盖defaultMaxPerRoute
//        connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 150);
        //消息约束
//        MessageConstraints messageConstraints = MessageConstraints.custom()
//                .setMaxHeaderCount(200)
//                .setMaxLineLength(2000)
//                .build();
        //Http connection相关配置
//        ConnectionConfig connectionConfig = ConnectionConfig.custom()
//                .setMalformedInputAction(CodingErrorAction.IGNORE)
//                .setUnmappableInputAction(CodingErrorAction.IGNORE)
//                .setCharset(Consts.UTF_8)
//                .setMessageConstraints(messageConstraints)
//                .build();
        //一般不修改HTTP connection相关配置，故不设置
        //connManager.setDefaultConnectionConfig(connectionConfig);

        httpClient = HttpClients.custom()
                .setDefaultHeaders(headers)
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public String doPostJson(String url, String jsonBody) {
        return doPostJson(url, Collections.emptyMap(), jsonBody);
    }

    public String doPostJson(String url, Map<String, String> params, String jsonBody) {
        HttpPost post = new HttpPost(url);

        StringEntity entity;
        try {
            entity = new StringEntity(jsonBody, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("string entity error", e);
            return null;
        }
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        post.setHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");

        post.setURI(URI.create(buildUrlParam(post.getURI().toString(), params)));
        post.setEntity(entity);

        return doRequest(post);
    }

    public HttpEntityCopy doPostJsonBytes(String url, Map<String, String> params, String jsonBody) {
        HttpPost post = new HttpPost(url);

        StringEntity entity;
        try {
            entity = new StringEntity(jsonBody, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("string entity error", e);
            return null;
        }
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        post.setHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");

        post.setURI(URI.create(buildUrlParam(post.getURI().toString(), params)));
        post.setEntity(entity);

        return doRequestObj(post);
    }

    public String doPostForm(String url, Map<String, String> headers, Map<String, String> params) {
        HttpPost post = new HttpPost(url);
        List<NameValuePair> values = new ArrayList<>();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            values.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        HttpEntity httpEntity = new UrlEncodedFormEntity(values, StandardCharsets.UTF_8);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> head : headers.entrySet()) {
                post.setHeader(head.getKey(), head.getValue());
            }
        }
        post.setHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
        post.setEntity(httpEntity);

        return doRequest(post);
    }

    public String doPostForm(String url, Map<String, String> params) {
        return doPostForm(url, null, params);
    }

    public String doGet(String url, Map<String, String> params) {
        HttpGet get = new HttpGet(URI.create(buildUrlParam(url, params)));
        return doRequest(get);
    }

    private String buildUrlParam(String url, Map<String, String> params) {
        if (params.isEmpty()) {
            return url;
        }
        // 设置参数
        List<NameValuePair> urlParams = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        try {
            String paramsString = EntityUtils.toString(new UrlEncodedFormEntity(urlParams, StandardCharsets.UTF_8));
            if (url.endsWith("/")) {
                return url.substring(0, url.length() - 1) + "?" + paramsString;
            }
            return url + "?" + paramsString;
        } catch (IOException e) {
            throw new AuthException(e);
        }
    }

    private String doRequest(HttpUriRequest request) {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                log.error("http post not ok,code :{}", statusLine.getStatusCode());
                return null;
            }

            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            log.error("error to exec http post form", e);
        }
        return null;
    }

    private HttpEntityCopy doRequestObj(HttpUriRequest request) {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                log.error("http post not ok,code :{}", statusLine.getStatusCode());
                return null;
            }

            HttpEntity entity = response.getEntity();
            byte[] bytes = entity.getContent().readAllBytes();

            HttpEntityCopy httpEntityCopy = new HttpEntityCopy();
            httpEntityCopy.setData(bytes);
            httpEntityCopy.setContentType(entity.getContentType() == null ? null : entity.getContentType().getValue());
            httpEntityCopy.setContentEncoding(entity.getContentEncoding() == null ? null : entity.getContentEncoding().getValue());

            return httpEntityCopy;
        } catch (Exception e) {
            log.error("error to exec http post form", e);
        }
        return null;
    }
//    public OkHttpClient client;
//
//    private OkHttpConfig okHttpConfig;
//
//    private void init() {
//        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
//        clientBuilder.connectTimeout(okHttpConfig.getConnectTimeout(), TimeUnit.SECONDS)
//                .writeTimeout(okHttpConfig.getWriteTimeout(), TimeUnit.SECONDS)
//                .readTimeout(okHttpConfig.getReadTimeout(), TimeUnit.SECONDS)
//                .connectionPool(new ConnectionPool(okHttpConfig.getMaxIdle(), okHttpConfig.getKeepAliveDuration(), TimeUnit.MINUTES));
//        client = clientBuilder.build();
//        client.dispatcher().setMaxRequests(okHttpConfig.getMaxRequests());
//        client.dispatcher().setMaxRequestsPerHost(okHttpConfig.getMaxRequestsPerHost());
//    }
//
//    public String commonPost(String url, String body) throws IOException {
//        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
//        RequestBody requestBody = RequestBody.create(body, mediaType);
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//
//        Response response = client.newCall(request).execute();
//        return Objects.requireNonNull(response.body()).string();
//    }


    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public void setSetSoTimeout(int setSoTimeout) {
        this.setSoTimeout = setSoTimeout;
    }

    public void setSoLinger(int soLinger) {
        this.soLinger = soLinger;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public void setSoKeepAlive(boolean soKeepAlive) {
        this.soKeepAlive = soKeepAlive;
    }

    public void setSoReuseAddress(boolean soReuseAddress) {
        this.soReuseAddress = soReuseAddress;
    }

    public void setConnReqTimeout(int connReqTimeout) {
        this.connReqTimeout = connReqTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }
}
