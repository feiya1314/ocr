package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.config.ChatConfig;
import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.context.ChatServiceResult;
import cn.easyocr.ai.chat.service.dao.mapper.ChatMsgsMapper;
import cn.easyocr.ai.chat.service.dao.po.ChatMsgs;
import cn.easyocr.ai.chat.service.enums.ChatGptModel;
import cn.easyocr.ai.chat.service.enums.StreamRespEvent;
import cn.easyocr.ai.chat.service.handler.ISseEventHandler;
import cn.easyocr.ai.chat.service.handler.StreamResult;
import cn.easyocr.ai.chat.service.helper.HttpClientHelper;
import cn.easyocr.ai.chat.service.listener.SseEvent;
import cn.easyocr.ai.chat.service.listener.SseEventListener;
import cn.easyocr.ai.chat.service.req.ChatGptReq;
import cn.easyocr.ai.chat.service.resp.Api2dChaGptResp;
import cn.easyocr.ai.chat.service.resp.ChatChoice;
import cn.easyocr.ai.chat.service.resp.GptStreamResp;
import cn.easyocr.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    @Autowired
    private ChatMsgsMapper chatMsgsMapper;

    @Override
    public ChatServiceResult chat(ChatContext chatContext) {
        ChatServiceResult result = new ChatServiceResult();
        GptStreamResp response = callChatApi(chatContext);
        result.setStreamingResponseBody(response);

        return result;
    }

    private GptStreamResp callChatApi(ChatContext chatContext) {
        GptStreamResp streamResponse = new GptStreamResp(chatContext);

        ChatGptReq chatGptReq = new ChatGptReq();
        chatGptReq.setModel(ChatGptModel.GPT_3_5_TURBO.getModel());
        chatGptReq.setMessages(chatContext.getReqMessages());
        chatGptReq.setStream(true);
        chatGptReq.setMax_tokens(config.getMaxTokens());

        String requestBody = JsonUtils.toJson(chatGptReq);

        okhttp3.MediaType mediaType = okhttp3.MediaType.Companion.parse("application/json;charset=UTF-8");
        okhttp3.RequestBody okHttpReqBody = okhttp3.RequestBody.Companion.create(requestBody, mediaType);
        Headers headers = new Headers.Builder()
                .add("Authorization", config.getApi2D().getApiKey())
                .build();

        Request request = new Request.Builder()
                .url(config.getApi2D().getUrl())
                .headers(headers)
                .post(okHttpReqBody)
                .build();
        log.info("call api2d for response");
        httpHelper.eventSourceFactory().newEventSource(request, buildEventListener(chatContext, streamResponse));

        return streamResponse;
    }

    private SseEventListener buildEventListener(ChatContext chatContext, GptStreamResp streamResponse) {
        ISseEventHandler<SseEvent> eventHandler = new ISseEventHandler<>() {
            private volatile boolean finishUpdate = false;

            @Override
            public void accept(SseEvent sseEvent) {
                String data = sseEvent.getData();
                if (data.equals("[DONE]")) {
                    log.debug("ISseEventHandler accept data [DONE]");

                    if (!finishUpdate) {
                        finishUpdate = true;
                        // 结束回答了，更新
                        responseFinish(chatContext);
                    }
                    this.onClose();
                    return;
                }
                Api2dChaGptResp response = JsonUtils.jsonToBean(data, Api2dChaGptResp.class);
                if (response == null || CollectionUtils.isEmpty(response.getChoices())) {
                    log.warn("sse event choices is empty");
                    // 这里先更新下，不确定有没有结束回答
                    responseFinish(chatContext);
                    return;
                }

                List<ChatChoice> choices = response.getChoices();
                for (ChatChoice chatChoice : choices) {
                    if (chatChoice.getFinishReason() != null && "stop".equals(chatChoice.getFinishReason())) {
                        log.debug("sse event choices FinishReason is stop");
                        if (!finishUpdate) {
                            finishUpdate = true;
                            // 结束回答了，更新
                            responseFinish(chatContext);
                        }
                        break;
                    }
                }

                try {
                    streamResponse.sseEvent.put(response);
                } catch (InterruptedException e) {
                    log.error("sseEvent put InterruptedException", e);
                }
            }

            @Override
            public void onClose() {
                log.debug("ISseEventHandler onClose");
                streamResponse.onComplete(StreamRespEvent.FINISH.getEvent(), "");
            }

            @Override
            public void onFailure(String msg) {
                log.error("ISseEventHandler onFailure msg : {}", msg);
                streamResponse.onComplete(StreamRespEvent.ERROR.getEvent(), "出了点错误，请稍后重试~");
                StreamResult streamResult = new StreamResult();
                streamResult.setStatus(StreamRespEvent.ERROR.getEvent());
                streamResult.setContent(msg);

                updateStreamResp(chatContext, streamResult);
            }
        };

        return new SseEventListener(eventHandler);
    }

    private void responseFinish(ChatContext chatContext) {
        String wholeText = chatContext.getRespWholeText();
        StreamResult streamResult = new StreamResult();
        streamResult.setStatus(StreamRespEvent.FINISH.getEvent());
        streamResult.setContent(wholeText);

        log.debug("ISseEventHandler responseFinish, update resp wholeText : {}", wholeText);
        updateStreamResp(chatContext, streamResult);
    }

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
