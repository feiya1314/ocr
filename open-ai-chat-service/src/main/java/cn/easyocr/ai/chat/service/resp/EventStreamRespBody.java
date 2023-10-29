package cn.easyocr.ai.chat.service.resp;

import cn.easyocr.ai.chat.service.enums.StreamEventState;
import cn.easyocr.common.enums.ResultCodeEnum;
import cn.easyocr.common.exception.ServiceException;
import cn.easyocr.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : feiya
 * @date : 2023/10/28
 * @description :
 */
@Slf4j
public abstract class EventStreamRespBody<R> implements StreamingResponseBody {
    private final SynchronousQueue<ChatStreamingEvent<R>> sseEvent = new SynchronousQueue<>();

    private volatile boolean working = true;

    private final Lock lock = new ReentrantLock();

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        while (working) {
            try {
                lock.lock();
                ChatStreamingEvent<R> event = sseEvent.take();
                if (event.getState() == StreamEventState.ERROR) {
                    return;
                }

                if (event.getState() == StreamEventState.FINISH && event.getData() == null) {
                    return;
                }

                beforeStreamWrite(event);

                String respJson = JsonUtils.toJson(event.getData()) + System.lineSeparator();
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

    protected abstract void beforeStreamWrite(ChatStreamingEvent<R> event);

    /**
     * sse 的响应处理完成后格式化为 ChatStreamingEvent ，这里  onStreamEvent 和 writeTo 不在同一个线程，注意线程安全问题
     *
     * @param event sse 响应处理和格式化的时间
     */
    public void onStreamEvent(ChatStreamingEvent<R> event) {
        try {
            if (event.getState() == StreamEventState.FINISH || event.getState() == StreamEventState.ERROR) {
                working = false;
            }
            // 这里working = false ，但是调用stream时，writeTo 因为working = false 可能已经结束了，所以offer 需要结束,
            // 如果使用 不带超时的方法时，会导致 sseEvent 一直阻塞，导致OKhttp线程泄漏, 而使用带超时时间的 offer 方法的话
            // 如果时间内没有被消费，则会取消，返回false
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
