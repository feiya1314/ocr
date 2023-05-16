package cn.easy.ocr.main.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2022/6/16
 * @description :
 */
@Slf4j
public class HttpUtil {
    private static final CloseableHttpClient httpClient;

    static {
        RequestConfig requestConfig = RequestConfig.DEFAULT;
//        List<Header> headers = new ArrayList<>();
//        headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()));
//        headers.add(new BasicHeader(HttpHeaders.ACCEPT, "*/*"));
//        headers.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, x-gzip, deflate"));
//        headers.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
        httpClient = HttpClients.createDefault();
//        httpClient = HttpClients.custom()
//                .setDefaultHeaders(headers)
//                .setDefaultRequestConfig(requestConfig)
//                .build();
    }

    public static String doPostJson(String url, String jsonBody) {
        HttpPost post = new HttpPost(url);

        StringEntity entity;
        try {
            entity = new StringEntity(jsonBody);
        } catch (Exception e) {
            log.error("string entity error", e);
            return null;
        }
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        post.setHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");

        post.setEntity(entity);
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                log.error("http post not ok,code :{}", statusLine.getStatusCode());
                return null;
            }

            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            log.error("error to exec http post json", e);
        }

        return null;
    }

    public static String doPostForm(String url, Map<String, String> headers, Map<String, String> params) {
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
        try (CloseableHttpResponse response = httpClient.execute(post)) {
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

    public static String doPostForm(String url, Map<String, String> params) {
        return doPostForm(url, null, params);
    }
}
