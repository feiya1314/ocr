package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.config.ChatConfig;
import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.enums.ChatGptModel;
import cn.easyocr.ai.chat.service.enums.ChatRole;
import cn.easyocr.ai.chat.service.enums.ChatSource;
import cn.easyocr.ai.chat.service.enums.ChatSourceKey;
import cn.easyocr.ai.chat.service.enums.StreamEventState;
import cn.easyocr.ai.chat.service.processor.ChatContextMsgProcessor;
import cn.easyocr.ai.chat.service.req.ChatGptReq;
import cn.easyocr.ai.chat.service.req.Message;
import cn.easyocr.ai.chat.service.resp.AiChatMsgResp;
import cn.easyocr.ai.chat.service.resp.Api2dChaGptResp;
import cn.easyocr.ai.chat.service.resp.ChatChoice;
import cn.easyocr.ai.chat.service.resp.ChatEventStreamRespBody;
import cn.easyocr.ai.chat.service.resp.ChatStreamingEvent;
import cn.easyocr.ai.chat.service.resp.EventStreamRespBody;
import cn.easyocr.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * @author : feiya
 * @date : 2023/10/29
 * @description :
 */
@Component(ChatSourceKey.API_2D)
@Slf4j
public class Api2DChatImpl extends AbsAiChatService<AiChatMsgResp> {
    @Autowired
    private ChatConfig config;

    @Autowired
    private ChatContextMsgProcessor processor;

    @PostConstruct
    public void init() {
        preChatProcessors = Collections.singletonList(processor);
    }


    @Override
    public ChatSource getChatSource() {
        return ChatSource.API_2D;
    }

    @Override
    protected StreamingResponseBody callChatApi(ChatContext chatContext) {
        EventStreamRespBody<AiChatMsgResp> streamResponse = new ChatEventStreamRespBody(chatContext);

        ChatGptReq chatGptReq = new ChatGptReq();
        chatGptReq.setModel(ChatGptModel.GPT_3_5_TURBO.getModel());
        chatGptReq.setMessages(chatContext.getReqMessages());
        chatGptReq.setStream(true);
        chatGptReq.setMax_tokens(config.getMaxTokens());

        String requestBody = JsonUtils.toJson(chatGptReq);

        okhttp3.MediaType mediaType = okhttp3.MediaType.Companion.parse("application/json;charset=UTF-8");
        okhttp3.RequestBody okHttpReqBody = okhttp3.RequestBody.Companion.create(requestBody, mediaType);
        Headers headers = new Headers.Builder()
                .add("Authorization", "Bearer " + chatContext.getKeyInfo().key)
                .build();

        Request request = new Request.Builder()
                .url(config.getApi2D().getUrl())
                .headers(headers)
                .post(okHttpReqBody)
                .build();
        log.info("call api2d for response");
        okHttpClientHelper.eventSourceFactory().newEventSource(request, buildEventListener(chatContext, streamResponse));

        return streamResponse;
    }

    @Override
    protected ChatStreamingEvent<AiChatMsgResp> streamRespToEvent(ChatContext chatContext, String StreamingRespData) {
        ChatStreamingEvent<AiChatMsgResp> event = new ChatStreamingEvent<>();
        if ("[DONE]".equals(StreamingRespData)) {
            log.debug("ISseEventHandler accept data [DONE]");
            event.setState(StreamEventState.CLOSE);

            return event;
        }

        Api2dChaGptResp response = JsonUtils.jsonToBean(StreamingRespData, Api2dChaGptResp.class);
        if (response == null || CollectionUtils.isEmpty(response.getChoices())) {
            log.warn("sse event choices is empty");
            // 这里先更新下，不确定有没有结束回答
            event.setState(StreamEventState.EMPTY);
            return event;
        }

        List<ChatChoice> chatChoices = response.getChoices();
        for (ChatChoice chatChoice : chatChoices) {
            if (chatChoice.getFinishReason() != null && "stop".equals(chatChoice.getFinishReason())) {
                log.debug("sse event choices FinishReason is stop");
                event.setState(StreamEventState.FINISH);
                break;
            }
        }
        AiChatMsgResp resp = new AiChatMsgResp();
        StringBuilder text = new StringBuilder();
        for (ChatChoice chatChoice : chatChoices) {
            Message msg = chatChoice.getDelta();
            if (msg != null && msg.getContent() != null) {
                text.append(msg.getContent());
            }
        }

        resp.setText(text.toString());
        resp.setRole(ChatRole.ASSISTANT.getRole());
//                resp.setParentMessageId("parent_id");
        resp.setConversationId(chatContext.getChatId());
        resp.setId(chatContext.getRespMsgId());

        event.setData(resp);
        return event;
    }

}
