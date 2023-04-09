package cn.easyocr.common.interceptor;

import cn.easyocr.common.utils.Constants;
import cn.easyocr.common.utils.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author : feiya
 * @date : 2022/9/11
 * @description :
 */
@Slf4j
public class RequestTraceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(Constants.REQUEST_TRACE_KEY, getRequestId(request));
        request.setAttribute(Constants.REQUEST_START_TIME, System.currentTimeMillis());
        String className = getClassName(handler);
        String methodName = getMethodName(handler);
        log.info("{}|{}|request start", className, methodName);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        // 把requestId添加到响应头，以便其它应用使用
        response.addHeader(Constants.REQUEST_TRACE_KEY, MDC.get(Constants.REQUEST_TRACE_KEY));
        long startTime = (long) request.getAttribute(Constants.REQUEST_START_TIME);
        long costTime = System.currentTimeMillis() - startTime;
        String className = getClassName(handler);
        String methodName = getMethodName(handler);

        log.info("{}|{}|{}|request finish", className, methodName, costTime);
        // 请求完成，从MDC中移除requestId
        MDC.remove(Constants.REQUEST_TRACE_KEY);
    }

    private String getMethodName(Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return "";
        }
        Method method = handlerMethod.getMethod();

        return method.getName();
    }

    private String getClassName(Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return "";
        }
        return handlerMethod.getBean().getClass().getName();
    }

    private String getRequestId(HttpServletRequest request) {
        String paramRequestId = request.getParameter(Constants.REQUEST_TRACE_KEY);
        String headerRequestId = request.getHeader(Constants.REQUEST_TRACE_KEY);
        // 根据请求参数或请求头判断是否有“request-id”，有则使用，无则创建
        String requestId;
        if (paramRequestId == null && headerRequestId == null) {
            requestId = UuidUtil.getUuid();
        } else {
            requestId = paramRequestId != null ? paramRequestId : headerRequestId;
        }
        return requestId;
    }
}
