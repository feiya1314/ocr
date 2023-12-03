package cn.easyocr.ai.chat.service.controller;

import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.context.ChatServiceResult;
import cn.easyocr.ai.chat.service.enums.ChatType;
import cn.easyocr.ai.chat.service.req.AiChatReq;
import cn.easyocr.ai.chat.service.req.ChatGptReq;
import cn.easyocr.ai.chat.service.req.Message;
import cn.easyocr.ai.chat.service.resp.Api2dChaGptResp;
import cn.easyocr.ai.chat.service.resp.ChatChoice;
import cn.easyocr.ai.chat.service.service.IAiChatService;
import cn.easyocr.ai.chat.service.util.HttpUtil;
import cn.easyocr.common.utils.JsonUtils;
import cn.easyocr.db.common.dao.annotation.ReqLogAnno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Random;

/**
 * @author : feiya
 * @description :
 * @since : 2023/4/10
 */
@Controller
@RequestMapping("/api/v1")
@Slf4j
public class AiChatController {
    @Autowired
    @Qualifier("aiChatServiceAdapter")
    private IAiChatService aiChatService;

    @PostMapping(value = "/chat-process", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ReqLogAnno(origin = "ai-chat", asyncReq = true)
    public ResponseEntity<StreamingResponseBody> chatProcess(@Valid @RequestBody AiChatReq aiChatReq, HttpServletRequest request) {
        log.info("chatProcess request start");
        ChatContext.ChatContextBuilder chatContextBuilder = ChatContext.builder()
                .aiChatReq(aiChatReq)
                .userId(HttpUtil.getUserId(request))
                .chatType(ChatType.getByType(aiChatReq.getChatType()));

        ChatServiceResult chatServiceResult = aiChatService.chat(chatContextBuilder.build());

        StreamingResponseBody streamResponse = chatServiceResult.getStreamingResponseBody();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.txt");

        return ResponseEntity.ok().headers(headers).body(streamResponse);
    }

    @PostMapping(value = "/chat-process-append", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ReqLogAnno(origin = "ai-chat", asyncReq = true)
    public ResponseEntity<StreamingResponseBody> chatProcessAppend(@Valid @RequestBody AiChatReq aiChatReq, HttpServletRequest request) {
        log.info("chatProcessAppend request start");
        ChatContext.ChatContextBuilder chatContextBuilder = ChatContext.builder()
                .aiChatReq(aiChatReq)
                .userId(HttpUtil.getUserId(request))
                .chatType(ChatType.getByType(aiChatReq.getChatType()));

        ChatServiceResult chatServiceResult = aiChatService.chat(chatContextBuilder.build());

        StreamingResponseBody streamResponse = chatServiceResult.getStreamingResponseBody();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.txt");

        return ResponseEntity.ok().headers(headers).body(streamResponse);
    }

    @PostMapping(value = "/chat-stream")
    public SseEmitter chatGptMock(@Valid @RequestBody ChatGptReq chatGptReq) {
        // 超时时间60s，超时后服务端主动关闭连接 todo cache请求连接
        SseEmitter emmitter = new SseEmitter(100 * 1000L);
        emmitter.onTimeout(() -> log.warn("emmitter timeout"));

        emmitter.onCompletion(() -> log.warn("emmitter onCompletion"));

        Random random = new Random();
        new Thread(() -> {
            try {
                String text =
                        "response for:" + chatGptReq.getMessages().get(chatGptReq.getMessages().size() - 1).getContent();
                char[] chars = text.toCharArray();
                int i = 1;
                Api2dChaGptResp api2dChaGptResp = new Api2dChaGptResp();
                api2dChaGptResp.setId("ididiidiidiid");
                api2dChaGptResp.setObject("chat.completion.chunk");
                api2dChaGptResp.setCreated(System.currentTimeMillis());
                api2dChaGptResp.setModel("gpt-3.5-turbo-0301");
                ChatChoice chatChoice = new ChatChoice();

                Message msg = new Message();
                chatChoice.setDelta(msg);
                api2dChaGptResp.setChoices(Collections.singletonList(chatChoice));
//                api2dChaGptResp.setUsage(new Usage());
                for (char c : chars) {
                    msg.setContent(String.valueOf(c));
                    SseEmitter.SseEventBuilder sb = SseEmitter.event().id(String.valueOf(i++)).data(JsonUtils.toJson(api2dChaGptResp));
                    emmitter.send(sb);
                    Thread.sleep(10 + random.nextInt(10));
                }
                SseEmitter.SseEventBuilder sb = SseEmitter.event().id("-1").data("[DONE]");
                emmitter.send(sb);
                emmitter.complete();
            } catch (Exception e) {
                log.error("emmitter send error", e);
            }
        }).start();

        return emmitter;
    }
}

