package cn.easy.ocr.main.service.thread;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : feiya
 * @date : 2022/10/5
 * @description :
 */
@Component
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
