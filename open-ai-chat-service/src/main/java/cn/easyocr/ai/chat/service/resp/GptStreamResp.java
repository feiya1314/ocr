package cn.easyocr.ai.chat.service.resp;

import cn.easyocr.ai.chat.service.enums.ChatRole;
import cn.easyocr.ai.chat.service.listener.SseEvent;
import cn.easyocr.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.SynchronousQueue;

/**
 * @author : feiya
 * @date : 2023/5/24
 * @description :
 */
@Slf4j
public class GptStreamResp implements StreamingResponseBody {
    public final SynchronousQueue<SseEvent> sseEvent = new SynchronousQueue<>();
    private volatile boolean working = true;
    private String text = "";

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        while (working) {
            try {
                SseEvent event = sseEvent.take();
                AiChatMsgResp resp = new AiChatMsgResp();
                resp.setId(event.getId());

                text = text + event.getData();
                resp.setText(text);
                resp.setRole(ChatRole.ASSISTANT.getRole());
                resp.setParentMessageId("parent_id");

                String respJson = JsonUtils.toJson(resp) + System.lineSeparator();
                outputStream.write(respJson.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            } catch (InterruptedException e) {
                log.error("GptStreamResponse sseEvent take Interrupted", e);
            }
        }
    }

    public void onComplete() {
        working = false;
        SseEvent.SseEventBuilder sseEventBuilder = SseEvent.builder()
                .id("-1")
                .event("finish")
                .data("");
        try {
            sseEvent.put(sseEventBuilder.build());
        } catch (InterruptedException e) {
            log.error("GptStreamResponse complete Interrupted", e);
        }
    }
}
