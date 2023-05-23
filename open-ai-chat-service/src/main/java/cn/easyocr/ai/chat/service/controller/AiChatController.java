package cn.easyocr.ai.chat.service.controller;

import cn.easyocr.ai.chat.service.config.ChatConfig;
import cn.easyocr.ai.chat.service.enums.ChatRole;
import cn.easyocr.ai.chat.service.handler.ISseEventHandler;
import cn.easyocr.ai.chat.service.listener.SseEvent;
import cn.easyocr.ai.chat.service.listener.SseEventListener;
import cn.easyocr.ai.chat.service.req.AiChatReq;
import cn.easyocr.ai.chat.service.req.ChatGptReq;
import cn.easyocr.ai.chat.service.resp.AiChatMsgResp;
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
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;
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

        ChatGptReq chatGptReq = new ChatGptReq();
        String requestBody = JsonUtils.toJson(chatGptReq);

        okhttp3.MediaType mediaType = okhttp3.MediaType.Companion.parse("application/json;charset=UTF-8");
        okhttp3.RequestBody okHttpReqBody = okhttp3.RequestBody.Companion.create(requestBody, mediaType);

        Request request = new Request.Builder()
                .url("http://127.0.0.1:8081/api/v1/stream")
                .post(okHttpReqBody)
                .build();

        GptStreamResponse streamResponse = new GptStreamResponse();
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

    public static class GptStreamResponse implements StreamingResponseBody {
        public final SynchronousQueue<SseEvent> sseEvent = new SynchronousQueue<>();
        private volatile boolean working = true;
        private String text = "";

        @Override
        public void writeTo(OutputStream outputStream) throws IOException {
            while (working) {
                try {
                    SseEvent event = sseEvent.take();
                    AiChatMsgResp resp = new AiChatMsgResp();
                    resp.setId(event.getId());

                    text = text + event.getData();
                    resp.setText(text);
                    resp.setRole(ChatRole.ASSISTANT.getRole());
                    resp.setParentMessageId("parent_id");

                    String respJson = JsonUtils.toJson(resp) + System.lineSeparator();
                    outputStream.write(respJson.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                } catch (InterruptedException e) {
                    log.error("GptStreamResponse sseEvent take Interrupted", e);
                }
            }
        }

        public void onComplete() {
            working = false;
            SseEvent.SseEventBuilder sseEventBuilder = SseEvent.builder()
                    .id("-1")
                    .event("finish")
                    .data("");
            try {
                sseEvent.put(sseEventBuilder.build());
            } catch (InterruptedException e) {
                log.error("GptStreamResponse complete Interrupted", e);
            }
        }
    }

    @PostMapping(value = "/chat-stream")
    @ReqLogAnno(origin = "ai-chat")
    public SseEmitter chatStreamResponse(@Valid @RequestBody AiChatReq aiChatReq) {
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
                    Thread.sleep(300 + random.nextInt(200));
                }
                emmitter.complete();
            } catch (Exception e) {
                log.error("emmitter send error", e);
            }
        });

        return emmitter;
    }
}

