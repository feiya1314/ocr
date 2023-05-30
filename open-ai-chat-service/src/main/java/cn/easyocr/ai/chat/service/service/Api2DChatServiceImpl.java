package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.config.ChatConfig;
import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.context.ChatServiceResult;
import cn.easyocr.ai.chat.service.handler.ISseEventHandler;
import cn.easyocr.ai.chat.service.helper.HttpClientHelper;
import cn.easyocr.ai.chat.service.listener.SseEvent;
import cn.easyocr.ai.chat.service.listener.SseEventListener;
import cn.easyocr.ai.chat.service.req.AiChatReq;
import cn.easyocr.ai.chat.service.resp.GptStreamResp;
import cn.easyocr.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Component("api2DChatService")
@Slf4j
public class Api2DChatServiceImpl implements IAiChatService {
    @Autowired
    private ChatConfig config;

    @Autowired
    private HttpClientHelper httpHelper;

    @Override
    public ChatServiceResult chat(ChatContext chatContext) {
        ChatServiceResult result = new ChatServiceResult();
        GptStreamResp response = callChatApi(chatContext.getAiChatReq());
        result.setStreamingResponseBody(response);

        return result;
    }

    private GptStreamResp callChatApi(AiChatReq aiChatReq) {
        GptStreamResp streamResponse = new GptStreamResp();

        String requestBody = JsonUtils.toJson(aiChatReq);

        okhttp3.MediaType mediaType = okhttp3.MediaType.Companion.parse("application/json;charset=UTF-8");
        okhttp3.RequestBody okHttpReqBody = okhttp3.RequestBody.Companion.create(requestBody, mediaType);

        Request request = new Request.Builder().url(config.getApi2D().getUrl()).post(okHttpReqBody).build();
        httpHelper.eventSourceFactory().newEventSource(request, buildEventListener(streamResponse));

        return streamResponse;
    }

    private SseEventListener buildEventListener(GptStreamResp streamResponse) {
        // todo 是否需要缓存 SseEventListener
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
                streamResponse.onComplete("finish", "");
                String wholeText = streamResponse.text;
                // todo 更新ai 回答结果
            }

            @Override
            public void onFailure() {
                String wholeText = "出了点小问题，请稍后重试";
                streamResponse.onComplete("error", wholeText);
                // todo 更新ai 回答结果
            }
        };

        return new SseEventListener(eventHandler);
    }
}
