package cn.easy.ocr.main.service.interceptor;

import cn.easy.ocr.main.service.annotation.ReqLogAnno;
import cn.easy.ocr.main.service.dao.mapper.OcrRequestLogMapper;
import cn.easy.ocr.main.service.dao.po.OcrRequestLog;
import cn.easy.ocr.main.service.thread.RequestLogThreadPool;
import cn.easy.ocr.main.service.utils.Constants;
import cn.easy.ocr.main.service.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : feiya
 * @date : 2022/10/4
 * @description :
 */
@Component
@Slf4j
public class RequestLogInterceptor implements HandlerInterceptor {
    @Autowired
    private OcrRequestLogMapper requestLogMapper;

    @Autowired
    private RequestLogThreadPool threadPool;
    private final Set<String> logPackages = new HashSet<>(Collections.singletonList("cn.easy.ocr.main.service.controller"));

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (checkNeedLog(handler)) {
                return true;
            }

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ReqLogAnno anno = method.getAnnotation(ReqLogAnno.class);

            String origin = anno.origin();
            String requestId = MDC.get(Constants.REQUEST_TRACE_KEY);
            String userId = request.getHeader(Constants.REQ_USER_ID);

            String ua = request.getHeader("user-agent");
            String refer = request.getHeader("referer");
            String ip = request.getHeader(Constants.REQ_REAL_IP);
            if (!StringUtils.hasText(ip)) {
                ip = request.getHeader(Constants.REQ_FORWARDED_IP);
            }

            OcrRequestLog reqLog = new OcrRequestLog();
            reqLog.setIp(ip);
            reqLog.setUserId(userId);
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
        return true;
    }

    private boolean checkNeedLog(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String pack = handlerMethod.getBean().getClass().getPackage().getName();
        if (!logPackages.contains(pack)) {
            return true;
        }
        Method method = handlerMethod.getMethod();
        ReqLogAnno anno = method.getAnnotation(ReqLogAnno.class);

        return anno == null;
    }
}
