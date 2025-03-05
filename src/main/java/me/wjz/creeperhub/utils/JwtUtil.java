package me.wjz.creeperhub.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class JwtUtil {
    @Value("${app.jwt.secretKey}")
    private String SECRET_KEY;
    @Value("${app.jwt.expiration}")
    private long EXPIRATION;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())//签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))//过期时间
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }
}


