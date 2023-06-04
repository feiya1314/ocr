package cn.easyocr.ai.chat.service.resp;

import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.enums.ChatRole;
import cn.easyocr.ai.chat.service.enums.StreamRespEvent;
import cn.easyocr.ai.chat.service.req.Message;
import cn.easyocr.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
    public final SynchronousQueue<Api2dChaGptResp> sseEvent = new SynchronousQueue<>();
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
                Api2dChaGptResp event = sseEvent.take();
                if (StreamRespEvent.FINISH.getEvent().equals(event.getId())) {
                    return;
                }

                if (StreamRespEvent.ERROR.getEvent().equals(event.getId())) {
                    return;
                }

                AiChatMsgResp resp = new AiChatMsgResp();
                List<ChatChoice> chatChoices = event.getChoices();
                for (ChatChoice chatChoice : chatChoices) {
                    Message msg = chatChoice.getDelta();
                    if (msg != null && msg.getContent() != null) {
                        text = text + msg.getContent();
                    }
                }

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

    public void onComplete(String reason, String text) {
        working = false;
        Api2dChaGptResp event = new Api2dChaGptResp();
        event.setId(reason);
        event.setObject(text);
        try {
            sseEvent.put(event);
        } catch (InterruptedException e) {
            log.error("GptStreamResponse complete Interrupted", e);
        }
    }
}
