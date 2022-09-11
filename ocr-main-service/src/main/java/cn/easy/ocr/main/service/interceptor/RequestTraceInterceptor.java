package cn.easy.ocr.main.service.interceptor;

import cn.easy.ocr.main.service.utils.Constants;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author : feiya
 * @date : 2022/9/11
 * @description :
 */
@Component
public class RequestTraceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(Constants.REQUEST_TRACE_KEY, getRequestId(request));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 把requestId添加到响应头，以便其它应用使用
        response.addHeader(Constants.REQUEST_TRACE_KEY, MDC.get(Constants.REQUEST_TRACE_KEY));
        // 请求完成，从MDC中移除requestId
        MDC.remove(Constants.REQUEST_TRACE_KEY);
    }

    private static String getRequestId(HttpServletRequest request) {
        String paramRequestId = request.getParameter(Constants.REQUEST_TRACE_KEY);
        String headerRequestId = request.getHeader(Constants.REQUEST_TRACE_KEY);
        // 根据请求参数或请求头判断是否有“request-id”，有则使用，无则创建
        String requestId;
        if (paramRequestId == null && headerRequestId == null) {
            requestId = simpleUuid();
        } else {
            requestId = paramRequestId != null ? paramRequestId : headerRequestId;
        }
        return requestId;
    }

    private static String simpleUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
