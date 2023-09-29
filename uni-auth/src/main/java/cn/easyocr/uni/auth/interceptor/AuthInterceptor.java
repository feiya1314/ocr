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
        log.info("auth req uri : {}", requestURI);
        if (whitelistUris.apply(requestURI)) {
            log.info("auth req uri in white list");
            return true;
        }

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(token)) {
            throw new AuthException(ResultCodeEnum.AUTH_FAILED);
        }

        Map<String, Object> claims = parseToken(token);
        Object claimUserId = claims.get(JwtUtil.USER_ID);
        if (claimUserId == null) {
            throw new AuthException(ResultCodeEnum.AUTH_FAILED);
        }

        String userId = request.getHeader(Constants.REQ_USER_ID);
        if(userId == null || userId.length() == 0 || !userId.equals(claimUserId.toString())){
            throw new AuthException(ResultCodeEnum.AUTH_FAILED);
        }
        return true;
    }

    private Map<String, Object> parseToken(String token) {
        try {
            Map<String, Object> claims = JwtUtil.decode(token, secret);
            Long timestamp = (Long) claims.get(JwtUtil.TOKEN_CREATE_TIME);
            return claims;
        } catch (ExpiredJwtException e) {
            log.error("token expired", e);
            throw new AuthException(ResultCodeEnum.AUTH_FAILED, "token expired");
        } catch (SignatureException e) {
            log.error("token sign error", e);
            throw new AuthException(ResultCodeEnum.AUTH_FAILED, "token invalid");
        } catch (Exception e) {
            log.error("token parse error", e);
            throw new AuthException(ResultCodeEnum.AUTH_FAILED, "token parse error");
        }
    }
}
