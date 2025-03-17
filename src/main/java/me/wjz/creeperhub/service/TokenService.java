package me.wjz.creeperhub.service;

import me.wjz.creeperhub.entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    public static final String TOKEN_PREFIX = "token:";
    @Autowired
    private RedisService redisService;

    public void setToken(Token token) {
        String key = TOKEN_PREFIX + token.getToken();
        redisService.putMap(key, token.toMap());
        redisService.expire(key, 60 * 60 * 24 * 30, TimeUnit.SECONDS);
    }

    public Token getToken(String token) {
        String key = TOKEN_PREFIX + token;
        return Token.fromMap(redisService.getMap(key));
    }

    public boolean validateToken(String token) {
        return redisService.hasKey(TOKEN_PREFIX + token);
    }

    public void deleteToken(String token) {
        redisService.delete(TOKEN_PREFIX + token);
    }
}
