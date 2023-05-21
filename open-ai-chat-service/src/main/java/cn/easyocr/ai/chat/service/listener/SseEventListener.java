package cn.easyocr.ai.chat.service.listener;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Slf4j
public class SseEventListener extends EventSourceListener {
    private Consumer<SseEvent> eventConsumer;

    public SseEventListener(Consumer<SseEvent> eventConsumer) {
        this.eventConsumer = eventConsumer;
    }

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        super.onClosed(eventSource);
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type,
                        @NotNull String data) {
        SseEvent sseEvent = new SseEvent();
        sseEvent.setId(id);
        sseEvent.setEvent(type);
        sseEvent.setData(data);

        eventConsumer.accept(sseEvent);
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

        this.onError(t, responseText);
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        super.onOpen(eventSource, response);
    }

    public void onError(Throwable t, String response) {

    }
}
