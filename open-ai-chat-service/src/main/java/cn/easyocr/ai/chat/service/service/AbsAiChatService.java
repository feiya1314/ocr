package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.context.ChatServiceResult;
import cn.easyocr.ai.chat.service.dao.mapper.ChatMsgsMapper;
import cn.easyocr.ai.chat.service.dao.po.ChatMsgs;
import cn.easyocr.ai.chat.service.enums.StreamEventState;
import cn.easyocr.ai.chat.service.handler.ISseEventHandler;
import cn.easyocr.ai.chat.service.handler.StreamResult;
import cn.easyocr.ai.chat.service.helper.OkHttpClientHelper;
import cn.easyocr.ai.chat.service.keys.ApiKeyManager;
import cn.easyocr.ai.chat.service.listener.SseEvent;
import cn.easyocr.ai.chat.service.listener.SseEventListener;
import cn.easyocr.ai.chat.service.processor.PreChatProcessor;
import cn.easyocr.ai.chat.service.resp.ChatStreamingEvent;
import cn.easyocr.ai.chat.service.resp.EventStreamRespBody;
import cn.easyocr.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSourceListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

/**
 * @author : feiya
 * @date : 2023/10/28
 * @description :
 */
@Slf4j
public abstract class AbsAiChatService<R> implements IAiChatService {
    @Autowired
    private ChatMsgsMapper chatMsgsMapper;

    @Autowired
    protected OkHttpClientHelper okHttpClientHelper;

    @Autowired
    private ApiKeyManager apiKeyManager;

    protected List<PreChatProcessor> preChatProcessors;

    @Override
    public final ChatServiceResult chat(ChatContext chatContext) {
        if (!CollectionUtils.isEmpty(preChatProcessors)) {
            for (PreChatProcessor preChatProcessor : preChatProcessors) {
                preChatProcessor.preChat(chatContext);
            }
        }
        ChatServiceResult result = new ChatServiceResult();
        StreamingResponseBody response = callChatApi(chatContext);
        result.setStreamingResponseBody(response);

        return result;
    }

    /**
     * 调用chat api 接口，同时返回 StreamingResponseBody
     *
     * @param chatContext 上下文
     * @return StreamingResponseBody
     */
    protected abstract StreamingResponseBody callChatApi(ChatContext chatContext);

    /**
     * 将stream 流返回的数据，根据steam的格式处理，不同的chat 服务返回的数据格式可能不同，处理后生成ChatStreamingEvent
     *
     * @param streamingRespData stream 流返回的一次数据
     * @return ChatStreamingEvent
     */
    protected abstract ChatStreamingEvent<R> streamRespToEvent(ChatContext chatContext, String streamingRespData);

    protected EventSourceListener buildEventListener(ChatContext chatContext, EventStreamRespBody<R> streamResponse) {
        ISseEventHandler<SseEvent> eventHandler = new ISseEventHandler<>() {
            private volatile boolean finishUpdate = false;

            @Override
            public void accept(SseEvent sseEvent) {
                String data = sseEvent.getData();
                ChatStreamingEvent<R> chatStreamingEvent = streamRespToEvent(chatContext, data);
                // 没有数据需要返回给前端，直接结束
                if (chatStreamingEvent.getState() == StreamEventState.CLOSE) {
                    if (!finishUpdate) {
                        finishUpdate = true;
                        // 结束回答了，更新
                        responseFinish(chatContext);
                    }
                    this.onClose();
                    return;
                }

                if (chatStreamingEvent.getState() == StreamEventState.EMPTY) {
                    // 这里先更新下，不确定有没有结束回答
                    responseFinish(chatContext);
                    return;
                }

                // stream 完成了，向前端返回当前的数据
                if (chatStreamingEvent.getState() == StreamEventState.FINISH) {
                    // 这里先更新下，不确定有没有结束回答
                    if (!finishUpdate) {
                        finishUpdate = true;
                        // 结束回答了，更新
                        responseFinish(chatContext);
                    }
                }

                streamResponse.onStreamEvent(chatStreamingEvent);
            }

            @Override
            public void onClose() {
                log.debug("ISseEventHandler onClose");
                ChatStreamingEvent<R> chatStreamingEvent = new ChatStreamingEvent<>();
                chatStreamingEvent.setState(StreamEventState.FINISH);
                streamResponse.onStreamEvent(chatStreamingEvent);
            }

            @Override
            public void onFailure(String msg) {
                log.error("ISseEventHandler onFailure msg : {}", msg);
                ChatStreamingEvent<R> chatStreamingEvent = new ChatStreamingEvent<>();
                chatStreamingEvent.setState(StreamEventState.ERROR);
                chatStreamingEvent.setMsg("出了点错误，请稍后重试~");

                streamResponse.onStreamEvent(chatStreamingEvent);

                StreamResult streamResult = new StreamResult();
                streamResult.setStatus(StreamEventState.ERROR.getEvent());
                streamResult.setContent(msg);

                chatContext.getKeyInfo().invalidInfo = chatContext.getKeyInfo().invalidInfo == null ? msg
                        : chatContext.getKeyInfo().invalidInfo + " " + msg;
                updateStreamResp(chatContext, streamResult);
                apiKeyManager.invalidKey(chatContext.getChatType(), chatContext.getKeyInfo());
                // todo 如果直接抛异常呢
            }
        };

        return new SseEventListener(eventHandler);
    }

    private void responseFinish(ChatContext chatContext) {
        String wholeText = chatContext.getRespWholeText();
        StreamResult streamResult = new StreamResult();
        streamResult.setStatus(StreamEventState.FINISH.getEvent());
        streamResult.setContent(wholeText);

        log.debug("ISseEventHandler responseFinish, update resp wholeText : {}", wholeText);
        updateStreamResp(chatContext, streamResult);
    }

    /**
     * 每次数据流回调时，同时更新数据库的该条请求
     *
     * @param chatContext  上下文
     * @param streamResult 数据流返回结果
     */
    private void updateStreamResp(ChatContext chatContext, StreamResult streamResult) {
        ChatMsgs chatMsgs = new ChatMsgs();
        chatMsgs.setChatId(chatContext.getChatId());
        chatMsgs.setMsgId(chatContext.getRespMsgId());
        chatMsgs.setContent(JsonUtils.toJson(streamResult));
        chatMsgs.setTimestamp(System.currentTimeMillis());

        log.debug("updateStreamResp,ChatId {} MsgId {} Content : {}", chatMsgs.getChatId(), chatMsgs.getMsgId(),
                chatMsgs.getContent());
        chatMsgsMapper.update(chatMsgs);
    }
}
