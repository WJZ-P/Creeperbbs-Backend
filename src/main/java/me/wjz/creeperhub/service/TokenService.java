package me.wjz.creeperhub.service;

import me.wjz.creeperhub.entity.Token;
import me.wjz.creeperhub.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    public static final String TOKEN_PREFIX = "token:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    public void setToken(Token token) {
        redisTemplate.opsForValue().set(TOKEN_PREFIX + token.getToken(), token.toString(), 60 * 60 * 24 * 30, TimeUnit.SECONDS);
    }
    public boolean validateToken(String token) {
        return redisTemplate.hasKey(TOKEN_PREFIX + token);
    }
}
