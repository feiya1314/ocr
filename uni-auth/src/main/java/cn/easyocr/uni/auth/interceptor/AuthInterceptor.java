package cn.easyocr.uni.auth.interceptor;

import cn.easyocr.common.enums.ResultCodeEnum;
import cn.easyocr.common.exception.AuthException;
import cn.easyocr.common.utils.Constants;
import cn.easyocr.uni.auth.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.function.Function;

/**
 * @author : feiya
 * @date : 2023/6/24
 * @description :
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    /**
     * 白名单地址过滤
     */
    private final Function<String, Boolean> whitelistUris;
    private final String secret;

    public AuthInterceptor(Function<String, Boolean> whitelistUris, String secret) {
        this.whitelistUris = whitelistUris;
        this.secret = secret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (whitelistUris.apply(requestURI)) {
            return true;
        }

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(token) || !validateToken(token)) {
            throw new AuthException(ResultCodeEnum.AUTH_FAILED);
        }

        String userId = request.getHeader(Constants.REQ_USER_ID);
        if (!StringUtils.hasText(userId)) {
            throw new AuthException(ResultCodeEnum.AUTH_FAILED);
        }

        return true;
    }

    private boolean validateToken(String token) {
        try {
            Map<String, Object> claims = JwtUtil.decode(token, secret);
            Long timestamp = (Long) claims.get(JwtUtil.TOKEN_CREATE_TIME);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("token expired", e);
            return false;
        } catch (SignatureException e) {
            log.error("token sign error", e);
            return false;
        } catch (Exception e) {
            log.error("token parse error", e);
            return false;
        }
    }
}
