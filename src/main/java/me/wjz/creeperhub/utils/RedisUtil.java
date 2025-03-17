package me.wjz.creeperhub.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.Post;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

@Component
public class RedisUtil {
    @Autowired
    private RedisService redisService;
    @Value("${server.public-ip}")
    private String publicIp;
    public static final String WEBSOCKET_USERID_TO_SESSION_KEY = "websocket:users_session";
    public static final String WEBSOCKET_SESSION_TO_IP_KEY = "websocket:sessions_ip";

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

    /**
     * 添加会话ID到Redis
     *
     * @param sessionId WebSocket会话的ID
     */
    public void addSession(Long userId, String sessionId) {
        if (sessionId != null && !sessionId.isEmpty()) {
            //这里应该用hash存储，因为方便根据用户ID获取对应的sessionId。
            Map<String, String> map = new HashMap<>();
            map.put(String.valueOf(userId), sessionId);
            redisService.putMap(WEBSOCKET_USERID_TO_SESSION_KEY, map);
            System.out.println("会话存入Redis，用户与会话ID：" + userId + ":" + sessionId);

            //同时存储一份sessionID和服务器IP的映射
            map.clear();
            map.put(sessionId, publicIp);
            redisService.putMap(WEBSOCKET_SESSION_TO_IP_KEY, map);
        }
    }

    /**
     * 从Redis移除会话ID
     *
     * @param sessionId WebSocket会话的ID
     */
    public void removeSession(Long userId, String sessionId) {
        if (sessionId != null && !sessionId.isEmpty()) {
            redisService.removeMap(WEBSOCKET_USERID_TO_SESSION_KEY, String.valueOf(userId));
            System.out.println("会话从Redis移除，会话ID：" + sessionId);
            //同时删掉session到Ip的映射。
            redisService.removeMap(WEBSOCKET_SESSION_TO_IP_KEY, sessionId);
        }
    }

    public String getSessionId(Long userId) {
        return (String) redisService.getMapValue(WEBSOCKET_USERID_TO_SESSION_KEY, String.valueOf(userId));
    }

    public String getServerIpBySessionId(String sessionId) {
        return (String) redisService.getMapValue(WEBSOCKET_SESSION_TO_IP_KEY, sessionId);
    }
}
