package cn.easyocr.ai.chat.service.resp;

import cn.easyocr.ai.chat.service.context.ChatContext;

/**
 * @author : feiya
 * @date : 2023/10/29
 * @description :
 */
public class ChatEventStreamRespBody extends EventStreamRespBody<AiChatMsgResp> {
    public String text = "";
    private final ChatContext chatContext;

    public ChatEventStreamRespBody(ChatContext chatContext) {
        this.chatContext = chatContext;
    }

    @Override
    protected void beforeStreamWrite(ChatStreamingEvent<AiChatMsgResp> event) {
        if (event.getData() == null) {
            return;
        }

        AiChatMsgResp resp = event.getData();
        String deltaText = resp.getText();
        text = text + deltaText;
        chatContext.setRespWholeText(text);
        resp.setText(text);
    }
}
