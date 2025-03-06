package me.wjz.creeperhub.service;

import me.wjz.creeperhub.entity.Token;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    public static final String TOKEN_PREFIX = "token:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void setToken(Token token) {
        String key = TOKEN_PREFIX + token.getToken();
        redisTemplate.opsForHash().putAll(key, token.toMap());
        redisTemplate.expire(key, 60 * 60 * 24 * 30, TimeUnit.SECONDS);
    }

    public boolean validateToken(String token) {
        return redisTemplate.hasKey(TOKEN_PREFIX + token);
    }
}
