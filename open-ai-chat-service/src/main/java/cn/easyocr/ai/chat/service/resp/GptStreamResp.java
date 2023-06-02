package cn.easyocr.ai.chat.service.resp;

import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.enums.ChatRole;
import cn.easyocr.ai.chat.service.listener.SseEvent;
import cn.easyocr.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : feiya
 * @date : 2023/5/24
 * @description :
 */
@Slf4j
public class GptStreamResp implements StreamingResponseBody {
    public final SynchronousQueue<SseEvent> sseEvent = new SynchronousQueue<>();
    private volatile boolean working = true;
    public String text = "";
    private final ChatContext chatContext;
    private final Lock lock = new ReentrantLock();

    public GptStreamResp(ChatContext chatContext) {
        this.chatContext = chatContext;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        while (working) {
            try {
                lock.lock();
                SseEvent event = sseEvent.take();
                AiChatMsgResp resp = new AiChatMsgResp();
                resp.setId(event.getId());

                text = text + event.getData();
                resp.setText(text);
                resp.setRole(ChatRole.ASSISTANT.getRole());
//                resp.setParentMessageId("parent_id");
                resp.setConversationId(chatContext.getChatId());
                resp.setId(chatContext.getRespMsgId());

                String respJson = JsonUtils.toJson(resp) + System.lineSeparator();
                outputStream.write(respJson.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            } catch (InterruptedException e) {
                log.error("GptStreamResponse sseEvent take Interrupted", e);
            } finally {
                lock.unlock();
            }
        }
    }

    public void onComplete(String event, String text) {
        working = false;
        SseEvent.SseEventBuilder sseEventBuilder = SseEvent.builder()
                .id("-1")
                .event(event)
                .data(text);
        try {
            sseEvent.put(sseEventBuilder.build());
        } catch (InterruptedException e) {
            log.error("GptStreamResponse complete Interrupted", e);
        }
    }
}
