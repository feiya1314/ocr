package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.req.ChatContext;
import cn.easyocr.ai.chat.service.resp.AiChatMsgResp;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.SseStreamListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
public class ChatGptServiceImpl implements IAiChatService{
    @Override
    public AiChatMsgResp chat(ChatContext chatContext) {
        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("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")
            //    .proxy(proxy)
                .timeout(900)
                .apiHost("https://api.openai.com/") //反向代理地址
                .build()
                .init();

        Message system = Message.ofSystem("你现在是一个诗人，专门写七言绝句");
        Message message = Message.of("写一段七言绝句诗，题目是：火锅！");

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(Arrays.asList(system, message))
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        Message res = response.getChoices().get(0).getMessage();
        System.out.println(res);


        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey("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")
              //  .proxy(proxy)
                .apiHost("https://api.openai.com/")
                .build()
                .init();

        SseEmitter sseEmitter = new SseEmitter(-1L);

        SseStreamListener listener = new SseStreamListener(sseEmitter);
       // Message message = Message.of(prompt);

        listener.setOnComplate(msg -> {
            //回答完成，可以做一些事情
        });
        chatGPTStream.streamChatCompletion(Arrays.asList(message), listener);

        return null;
    }
}
