package cn.easy.ocr.main.service.interceptor;

import cn.easy.ocr.main.service.dao.mapper.OcrRequestLogMapper;
import cn.easy.ocr.main.service.dao.po.OcrRequestLog;
import cn.easy.ocr.main.service.utils.Constants;
import cn.easy.ocr.main.service.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String requestId = MDC.get(Constants.REQUEST_TRACE_KEY);
            String userId = request.getHeader(Constants.REQ_USER_ID);

            String ua = request.getHeader("user-agent");
            String refer = request.getHeader("referer");
            String ip = request.getHeader(Constants.REQ_REAL_IP);
            if (!StringUtils.hasText(ip)) {
                ip = request.getHeader(Constants.REQ_FORWARDED_IP);
            }

            OcrRequestLog log = new OcrRequestLog();
            log.setIp(ip);
            log.setUserId(userId);
            log.setRequestId(requestId);
            log.setUa(ua);
            log.setRefer(refer);
            log.setTimestamp(System.currentTimeMillis());
            log.setSdevId(null);
            log.setDeviceData(null);
            log.setPtd(TimeUtil.getPtd());

            // todo thread pool
            requestLogMapper.insert(log);
        } catch (Exception e) {
            log.error("log req error", e);
        }
        return true;
    }
}
