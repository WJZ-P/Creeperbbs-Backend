package me.wjz.creeperhub.utils;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static String SECRET_KEY;
    private static long EXPIRATION;

    @Autowired
    private UserService userService;

    @Value("${app.jwt.secret-key}")//这两个是实例字段，用于接收value注入。
    private String secret_key;
    @Value("${app.jwt.expiration}")
    private long expiration;

    @PostConstruct
    protected void init() {
        SECRET_KEY = secret_key;//实例字段复制到静态字段
        EXPIRATION = expiration;
    }

    public static String generateJwt(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())//签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))//过期时间
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public static String verifyJwt(String jwt) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new CreeperException(ErrorType.JWT_EXPIRED);
        } catch (JwtException e) {
            throw new CreeperException(ErrorType.JWT_PARSE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CreeperException(ErrorType.UNKNOWN_ERROR);
        }
    }

    public static boolean verlidateToken(String token) {
        try {

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}


