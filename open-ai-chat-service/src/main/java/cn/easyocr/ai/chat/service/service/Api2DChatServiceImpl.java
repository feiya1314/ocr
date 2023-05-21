package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.req.ChatContext;
import cn.easyocr.ai.chat.service.resp.AiChatMsgResp;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Component
public class Api2DChatServiceImpl implements IAiChatService {
    @Override
    public AiChatMsgResp chat(ChatContext chatContext) {
        CloseableHttpClient httpClient;
        //httpClient.execute()
        return null;
    }
}
