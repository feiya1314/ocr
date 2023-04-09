package cn.easyocr.common.thread;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * @author : feiya
 * @date : 2022/10/5
 * @description :
 */
public class RequestLogThreadPool {
    private final ThreadFactory threadFactory = new CustomizableThreadFactory("ocr-request-log-pool-");
    private final int coreSize = Runtime.getRuntime().availableProcessors() + 1;
    private final int maxSize = Runtime.getRuntime().availableProcessors() * 2;
    private final int queueSize = 1000;

    private final long keepAliveTime = 30;

    private final ExecutorService threadPool = new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize), threadFactory);

    public void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }

    public <T> Future<T> submit(Callable<T> callable) {
        return threadPool.submit(callable);
    }
}
