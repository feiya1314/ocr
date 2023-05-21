package cn.easyocr.ai.chat.service.listener;

import com.alibaba.fastjson.JSON;
import com.plexpt.chatgpt.entity.chat.ChatChoice;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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
        super.onFailure(eventSource, t, response);
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        super.onOpen(eventSource, response);
    }
}
