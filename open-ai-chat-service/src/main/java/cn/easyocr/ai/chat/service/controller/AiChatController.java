package cn.easyocr.ai.chat.service.controller;

import cn.easyocr.ai.chat.service.config.ChatConfig;
import cn.easyocr.ai.chat.service.handler.ISseEventHandler;
import cn.easyocr.ai.chat.service.listener.SseEvent;
import cn.easyocr.ai.chat.service.listener.SseEventListener;
import cn.easyocr.ai.chat.service.req.AiChatReq;
import cn.easyocr.ai.chat.service.req.ChatGptReq;
import cn.easyocr.ai.chat.service.resp.GptStreamResp;
import cn.easyocr.common.utils.JsonUtils;
import cn.easyocr.db.common.dao.annotation.ReqLogAnno;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author : feiya
 * @description :
 * @since : 2023/4/10
 */
@Controller
@Slf4j
public class AiChatController {
    @Autowired
    private ChatConfig config;

    @PostMapping(value = "/chat-process", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ReqLogAnno(origin = "ai-chat")
    public ResponseEntity<StreamingResponseBody> chatProcess(@Valid @RequestBody AiChatReq aiChatReq) {
        log.info("chatProcess request start");
        ChatConfig.ChatGpt gpt = config.getChatGpt();

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(60, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS);
        clientBuilder.readTimeout(60, TimeUnit.SECONDS);

        OkHttpClient client = clientBuilder.build();
        EventSource.Factory factory = EventSources.createFactory(client);

        // ChatGptReq chatGptReq = new ChatGptReq();
        String requestBody = JsonUtils.toJson(aiChatReq);

        okhttp3.MediaType mediaType = okhttp3.MediaType.Companion.parse("application/json;charset=UTF-8");
        okhttp3.RequestBody okHttpReqBody = okhttp3.RequestBody.Companion.create(requestBody, mediaType);

        Request request = new Request.Builder()
                .url(gpt.getUrl())
                .post(okHttpReqBody)
                .build();

        GptStreamResp streamResponse = new GptStreamResp();
        ISseEventHandler<SseEvent> eventHandler = new ISseEventHandler<>() {
            @Override
            public void accept(SseEvent sseEvent) {
                try {
                    streamResponse.sseEvent.put(sseEvent);
                } catch (InterruptedException e) {
                    log.error("sseEvent put InterruptedException", e);
                }
            }

            @Override
            public void onClose() {
                streamResponse.onComplete();
            }

            @Override
            public void onFailure() {

            }
        };
        SseEventListener sseEventListener = new SseEventListener(eventHandler);

        factory.newEventSource(request, sseEventListener);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.txt");

        return ResponseEntity.ok().headers(headers).body(streamResponse);
    }

    @PostMapping(value = "/chat-stream")
    @ReqLogAnno(origin = "ai-chat")
    public SseEmitter chatGptMock(@Valid @RequestBody AiChatReq aiChatReq) {
        // 超时时间60s，超时后服务端主动关闭连接 todo cache请求连接
        SseEmitter emmitter = new SseEmitter(60 * 1000L);
        emmitter.onTimeout(() -> {
            log.warn("emmitter timeout");
        });

        emmitter.onCompletion(() -> {
            log.warn("emmitter onCompletion");
        });

        Random random = new Random();
        new Thread(() -> {
            try {
                String text = "response for:" + aiChatReq.getPrompt();
                char[] chars = text.toCharArray();
                int i = 1;
                for (char c : chars) {
                    SseEmitter.SseEventBuilder sb = SseEmitter.event().id(String.valueOf(i++)).data(String.valueOf(c));
                    emmitter.send(sb);
                    Thread.sleep(200 + random.nextInt(100));
                }
                emmitter.complete();
            } catch (Exception e) {
                log.error("emmitter send error", e);
            }
        }).start();

        return emmitter;
    }
}

