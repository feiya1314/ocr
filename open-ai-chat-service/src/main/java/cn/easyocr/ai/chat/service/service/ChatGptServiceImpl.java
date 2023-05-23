package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.handler.ISseEventHandler;
import cn.easyocr.ai.chat.service.listener.SseEvent;
import cn.easyocr.ai.chat.service.listener.SseEventListener;
import cn.easyocr.ai.chat.service.req.ChatContext;
import cn.easyocr.ai.chat.service.req.ChatGptReq;
import cn.easyocr.ai.chat.service.resp.AiChatMsgResp;
import cn.easyocr.common.utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.concurrent.TimeUnit;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
public class ChatGptServiceImpl implements IAiChatService {
    @Override
    public AiChatMsgResp chat(ChatContext chatContext) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(60, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS);
        clientBuilder.readTimeout(60, TimeUnit.SECONDS);

        OkHttpClient client = clientBuilder.build();
        EventSource.Factory factory = EventSources.createFactory(client);

        ChatGptReq chatGptReq = new ChatGptReq();
        String requestBody = JsonUtils.toJson(chatGptReq);

        okhttp3.MediaType mediaType = okhttp3.MediaType.Companion.parse("application/json;charset=UTF-8");
        okhttp3.RequestBody okHttpReqBody = RequestBody.Companion.create(requestBody, mediaType);

        Request request = new Request.Builder()
                .url("http://127.0.0.1:8081/api/v1/stream")
                .post(okHttpReqBody)
                .build();

      //  GptStreamResponse streamResponse = new GptStreamResponse();
        ISseEventHandler<SseEvent> eventHandler = new ISseEventHandler<>() {
            @Override
            public void accept(SseEvent sseEvent) {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onFailure() {

            }
        };

        SseEventListener sseEventListener = new SseEventListener(eventHandler);

        factory.newEventSource(request,sseEventListener);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.txt");

        return null;
//        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
//                .timeout(600)
//                .apiKey("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")
//                //  .proxy(proxy)
//                .apiHost("https://api.openai.com/")
//                .build()
//                .init();
//
//        SseEmitter sseEmitter = new SseEmitter(-1L);
//
//        SseStreamListener listener = new SseStreamListener(sseEmitter);
//        // Message message = Message.of(prompt);
//
//        listener.setOnComplate(msg -> {
//            //回答完成，可以做一些事情
//        });
//        chatGPTStream.streamChatCompletion(Arrays.asList(message), listener);

       // return ResponseEntity.ok().headers(headers).body(streamResponse);
    }


}
