package me.wjz.creeperhub.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.Post;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

@Component
public class RedisUtil {
    @Autowired
    private RedisService redisService;

    public User getUser(String token) {
        Map<Object, Object> map = redisService.getMap("token:" + token);
        String userId = String.valueOf(map.get("userId"));
        Map<Object, Object> userInfo = redisService.getMap("user:" + userId);
        return User.fromMap(userInfo);
    }

    public <T> T executeWithLock(String key, int expireTime, Callable<T> callable) {
        while (true) {
            if (redisService.setIfAbsent("lock:" + key, 1, expireTime)) {
                try {
                    return callable.call();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CreeperException(ErrorType.UNKNOWN_ERROR);
                } finally {
                    redisService.delete(key);
                }
            } else {
                try {
                    Thread.sleep(50 + new Random().nextInt(50));
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public Post getPost(String key) {
        String json = redisService.getString(key);
        if (json == null) return null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, Post.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
