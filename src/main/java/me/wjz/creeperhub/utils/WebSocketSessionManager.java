package me.wjz.creeperhub.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final RedisUtil redisUtil;

    @Autowired
    public WebSocketSessionManager(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public void addSession(WebSocketSession session) {
        String sessionId = session.getId();
        Long userId = (Long) session.getAttributes().get("userId");
        sessions.put(sessionId, session);
        if (userId != null) {//本地添加缓存，同时在redis中也缓存一份。
            redisUtil.addSession(userId, sessionId);
            //同时还要缓存一份session到服务器IP的映射。

        }
    }

    public void removeSession(WebSocketSession session) {
        String sessionId = session.getId();
        sessions.remove(sessionId);//删除本地缓存
        Long userId = (Long) session.getAttributes().get("userId");
        redisUtil.removeSession(userId,sessionId);//删除redis中的缓存
    }

    public WebSocketSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }
}