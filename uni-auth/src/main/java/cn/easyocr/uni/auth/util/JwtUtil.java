package cn.easyocr.uni.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2023/6/18
 * @description :
 */
public class JwtUtil {
    public static void decode(String token, String secret) {
//        String token = new String(Base64.getDecoder().decode(base64Token.getBytes(StandardCharsets.UTF_8)));
        Key signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        Jws<Claims> parse = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build().parseClaimsJws(token);

        Claims body = parse.getBody();
        System.out.println(body);
    }

    public static String genToken(Map<String, Object> claims, String secret, long expiration) {
        Date expiredDate = new Date(System.currentTimeMillis() + expiration);

        Key signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        claims.put("timestamp", System.currentTimeMillis());
        // signWith 设置密钥 ，JJWT确定 secret 所允许的最安全算法
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(signingKey)
                .compact();

//        return Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
        return token;
    }
}
