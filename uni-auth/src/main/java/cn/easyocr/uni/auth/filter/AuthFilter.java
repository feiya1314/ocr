package cn.easyocr.uni.auth.filter;

import cn.easyocr.uni.auth.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

/**
 * @author : feiya
 * @date : 2023/6/24
 * @description :
 */
@Slf4j
public class AuthFilter implements Filter {
    /**
     * 白名单地址过滤
     */
    private final Function<String, Boolean> whitelistUris;
    private final String secret;

    public AuthFilter(Function<String, Boolean> whitelistUris, String secret) {
        this.whitelistUris = whitelistUris;
        this.secret = secret;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest httpRequest) || !(response instanceof HttpServletResponse httpResponse)) {
            log.warn("not http req");
            chain.doFilter(request, response);
            return;
        }

        String requestURI = httpRequest.getRequestURI();
        if (whitelistUris.apply(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(token)) {
            httpResponse.setHeader("location", "http://127.0.0.1:3002/");

            httpResponse.sendRedirect("http://127.0.0.1:3002/");
            return;
        }

        if (!validateToken(token)) {
            httpResponse.setHeader("location", "http://127.0.0.1:3002/");

            httpResponse.sendRedirect("http://127.0.0.1:3002/");
            return;
        }

        chain.doFilter(request, response);
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
