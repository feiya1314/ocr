package cn.easyocr.ai.chat.service.resp;

import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.enums.ChatRole;
import cn.easyocr.ai.chat.service.enums.StreamRespEvent;
import cn.easyocr.ai.chat.service.req.Message;
import cn.easyocr.common.enums.ResultCodeEnum;
import cn.easyocr.common.exception.ServiceException;
import cn.easyocr.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : feiya
 * @date : 2023/5/24
 * @description :
 */
@Slf4j
public class GptStreamResp implements StreamingResponseBody {
    private final SynchronousQueue<Api2dChaGptResp> sseEvent = new SynchronousQueue<>();
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

                chatContext.setRespWholeText(text);
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
                throw new ServiceException(ResultCodeEnum.OCR_SERVICE_ERROR);
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * sse完成的请求，这里  onComplete 和 writeTo 不在同一个线程，注意线程安全问题
     *
     * @param reason 结束原因
     * @param text   结束描述
     */
    public void onComplete(String reason, String text) {
        log.debug("stream resp onComplete");
        working = false;
        Api2dChaGptResp event = new Api2dChaGptResp();
        event.setId(reason);
        event.setObject(text);

        stream(event);
    }

    /**
     * 新的数据流过来
     *
     * @param event 新数据
     */
    public void stream(Api2dChaGptResp event) {
        try {
            // 这里onComplete中working = false ，但是调用stream时，writeTo 因为working = false 可能已经结束了，所以offer 需要结束,
            // 如果使用 不带超时的方法时，会导致 sseEvent 一直阻塞，导致OKhttp线程泄漏
            int eventOfferTimeout = 3;
            boolean offered = sseEvent.offer(event, eventOfferTimeout, TimeUnit.SECONDS);
            if (!offered) {
                log.debug("stream offer event offered false ");
            }
        } catch (Exception e) {
            log.error("stream offer event error", e);
        }
    }
}
