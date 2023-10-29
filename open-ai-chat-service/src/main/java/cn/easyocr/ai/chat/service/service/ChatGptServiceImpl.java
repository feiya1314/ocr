package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.context.ChatServiceResult;
import cn.easyocr.ai.chat.service.enums.ChatSource;
import cn.easyocr.ai.chat.service.enums.ChatSourceKey;
import cn.easyocr.ai.chat.service.req.ChatGptReq;
import cn.easyocr.common.utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Component(ChatSourceKey.OPEN_AI)
public class ChatGptServiceImpl implements IAiChatService {
    @Override
    public ChatSource getChatSource() {
        return ChatSource.OPEN_AI;
    }

    @Override
    public ChatServiceResult chat(ChatContext chatContext) {
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

//        GptStreamResponse streamResponse = new GptStreamResponse();
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
//         Message message = Message.of(chatGptReq.getModel());
//
//        listener.setOnComplate(msg -> {
//            //回答完成，可以做一些事情
//        });
//        chatGPTStream.streamChatCompletion(Arrays.asList(message), listener);

        return null;


       // return ResponseEntity.ok().headers(headers).body(streamResponse);
    }


}
