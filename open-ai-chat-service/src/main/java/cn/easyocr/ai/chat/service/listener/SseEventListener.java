package cn.easyocr.ai.chat.service.listener;

import cn.easyocr.ai.chat.service.handler.ISseEventHandler;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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

        String responseText = "";

        if (Objects.nonNull(response)) {
            responseText = response.body().toString();
        }

        log.error("response：{}", responseText);

        String forbiddenText = "Your access was terminated due to violation of our policies";

        if (StrUtil.contains(responseText, forbiddenText)) {
            log.error("Chat session has been terminated due to policy violation");
            log.error("检测到号被封了");
        }

        String overloadedText = "That model is currently overloaded with other requests.";

        if (StrUtil.contains(responseText, overloadedText)) {
            log.error("检测到官方超载了，赶紧优化你的代码，做重试吧");
        }

        this.eventConsumer.onFailure(overloadedText);
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        super.onOpen(eventSource, response);
    }
}
