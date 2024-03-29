package cn.easyocr.uni.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2023/6/18
 * @description :
 */
public class JwtUtil {
    private static final String BEARER = "Bearer";
    public static final String TOKEN_CREATE_TIME = "timestamp";
    public static final String USER_ID = "userId";

    public static Map<String, Object> decode(String token, String secret) {
        String tokenBody = token.split(" ")[1];
//        String token = new String(Base64.getDecoder().decode(base64Token.getBytes(StandardCharsets.UTF_8)));
        Key signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        Jws<Claims> parse = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build().parseClaimsJws(tokenBody);

        Claims body = parse.getBody();
        return new HashMap<>(body);
    }

    public static String genToken(Map<String, Object> claims, String secret, long expiration) {
        Date expiredDate = new Date(expiration);

        Key signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        claims.put(TOKEN_CREATE_TIME, System.currentTimeMillis());
        // signWith 设置密钥 ，JJWT确定 secret 所允许的最安全算法
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(signingKey)
                .compact();

//        return Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
        return BEARER + " " + token;
    }
}
