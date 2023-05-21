package cn.easyocr.ai.chat.service.controller;

import cn.easyocr.ai.chat.service.config.ChatConfig;
import cn.easyocr.ai.chat.service.req.AiChatReq;
import cn.easyocr.ai.chat.service.resp.AiChatMsgResp;
import cn.easyocr.common.utils.JsonUtils;
import cn.easyocr.db.common.dao.annotation.ReqLogAnno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

/**
 * @author : feiya
 * @description :
 * @since : 2023/4/10
 */
@Controller
@Slf4j
public class AiChatController {
    @Autowired
    private ChatConfig config;

    @PostMapping(value = "/chat-process", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ReqLogAnno(origin = "ai-chat")
    public ResponseEntity<StreamingResponseBody> chatProcess(@Valid @RequestBody AiChatReq aiChatReq,
                                                             HttpServletResponse response, HttpServletRequest request) {
        log.info("chatProcess request start");

        ChatConfig.ChatGpt gpt = config.getChatGpt();
        // OcrResultVo vo = ocrDelegte.invokeOcr(request);
        StreamingResponseBody responseBody = outputStream -> {
            String text = "我是GPT3.5系统版本，是一种自然语言处理人工智能技术。我的主要功能是理解和生成自然语言文本，如回答问题、编写文章";
            char[] chars = text.toCharArray();

            AiChatMsgResp resp = new AiChatMsgResp();
            resp.setId("7GqvCVoecxmttgaJDqaV8auFyN0Yp");
            resp.setText("");
            resp.setRole("assistant");
            resp.setParentMessageId("66639c80-b1bf-4a84-b76f-43efaeb4bbaf");

            for (char c : chars) {
                resp.setText(resp.getText() + c);
                String responseJson = JsonUtils.toJson(resp) + System.lineSeparator();
                outputStream.write(responseJson.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();

            }
        };


        HttpHeaders headers = new HttpHeaders();
//        headers.set(HttpHeaders.TRANSFER_ENCODING, null);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.txt");

        log.info("chatProcess request finish");
        return ResponseEntity.ok().headers(headers).body(responseBody);


    }
}

