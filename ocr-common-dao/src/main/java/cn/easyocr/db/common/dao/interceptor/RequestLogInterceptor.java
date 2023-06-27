package cn.easyocr.db.common.dao.interceptor;

import cn.easyocr.common.thread.RequestLogThreadPool;
import cn.easyocr.common.utils.Constants;
import cn.easyocr.common.utils.TimeUtil;
import cn.easyocr.db.common.dao.annotation.ReqLogAnno;
import cn.easyocr.db.common.dao.mapper.OcrRequestLogMapper;
import cn.easyocr.db.common.dao.po.OcrRequestLog;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author : feiya
 * @date : 2022/10/4
 * @description :
 */
@Slf4j
public class RequestLogInterceptor implements AsyncHandlerInterceptor {
    private final OcrRequestLogMapper requestLogMapper;

    private final RequestLogThreadPool threadPool;

    public RequestLogInterceptor(OcrRequestLogMapper requestLogMapper, RequestLogThreadPool threadPool) {
        this.requestLogMapper = requestLogMapper;
        this.threadPool = threadPool;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Method method = handlerMethod.getMethod();
        ReqLogAnno anno = method.getAnnotation(ReqLogAnno.class);
        // 异步请求不在此记录
        if (anno == null || anno.asyncReq()) {
            return true;
        }

        recordRequest(request, handlerMethod);
        return true;
    }


    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return;
        }

        Method method = handlerMethod.getMethod();
        ReqLogAnno anno = method.getAnnotation(ReqLogAnno.class);
        if (anno == null || !anno.asyncReq()) {
            return;
        }

        recordRequest(request, handlerMethod);
    }

    private void recordRequest(HttpServletRequest request, HandlerMethod handlerMethod) {
        try {
            Method method = handlerMethod.getMethod();
            ReqLogAnno anno = method.getAnnotation(ReqLogAnno.class);

            String origin = anno.origin();
            String requestId = MDC.get(Constants.REQUEST_TRACE_KEY);
            String userId = request.getHeader(Constants.REQ_USER_ID);
            String ip = request.getRemoteAddr();
            String remoteHost = request.getRemoteHost();
            String ua = request.getHeader("user-agent");
            String refer = request.getHeader("referer");
            String realIp = request.getHeader(Constants.REQ_REAL_IP);
            String fordIp = request.getHeader(Constants.REQ_FORWARDED_IP);

            log.info("getRemoteAddr :{},remoteHost:{},realIp:{},fordIp:{}", ip, remoteHost, realIp, fordIp);
            OcrRequestLog reqLog = new OcrRequestLog();
            reqLog.setIp(StringUtils.hasText(realIp) ? realIp : remoteHost);
            reqLog.setUserId(userId == null ? null : Long.parseLong(userId));
            reqLog.setRequestId(requestId);
            reqLog.setOrigin(origin);
            reqLog.setUa(ua);
            reqLog.setRefer(refer);
            reqLog.setTimestamp(System.currentTimeMillis());
            reqLog.setSdevId(null);
            reqLog.setDeviceData(null);
            reqLog.setPtd(TimeUtil.getPtd());

            threadPool.execute(() -> {
                try {
                    requestLogMapper.insert(reqLog);
                } catch (Exception e) {
                    log.error("Error inserting log to db", e);
                }
            });
        } catch (Exception e) {
            log.error("log req error", e);
        }
    }
}
