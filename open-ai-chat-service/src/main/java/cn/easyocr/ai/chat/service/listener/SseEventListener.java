package cn.easyocr.ai.chat.service.listener;

import cn.easyocr.ai.chat.service.handler.ISseEventHandler;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Slf4j
public class SseEventListener extends EventSourceListener {
    private final ISseEventHandler<SseEvent> eventConsumer;

    public SseEventListener(ISseEventHandler<SseEvent> eventConsumer) {
        this.eventConsumer = eventConsumer;
    }

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        this.eventConsumer.onClose();
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type,
                        @NotNull String data) {
        SseEvent.SseEventBuilder sseEvent = SseEvent.builder()
                .id(id)
                .event(type)
                .data(data);

        this.eventConsumer.accept(sseEvent.build());
    }

    @Override
    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        log.error("Stream connection error", t);

        if (response == null) {
            log.error("Stream response is null");
            this.eventConsumer.onFailure("Stream response is null");
            return;
        }
        String responseText = "";
        try {
            if (response.body() == null) {
                log.error("Stream response body is null");
                responseText = "Stream response body is null";
            } else {
                responseText = response.body().string();
            }
        } catch (IOException e) {
            log.error("error get stream response body");
        }

        log.error("chat stream response from remote ,code :{}, body: {}", response.code(), responseText);

        String forbiddenText = "Your access was terminated due to violation of our policies";

        if (responseText.contains(forbiddenText)) {
            log.error("Chat session has been terminated due to policy violation");
            log.error("检测到号被封了");
        }

        String overloadedText = "That model is currently overloaded with other requests.";

        if (responseText.contains(overloadedText)) {
            log.error("检测到官方超载了，赶紧优化你的代码，做重试吧");
        }

        this.eventConsumer.onFailure(responseText);
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        super.onOpen(eventSource, response);
    }
}
