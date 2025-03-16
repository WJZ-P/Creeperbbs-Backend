package me.wjz.creeperhub.handler;

import jakarta.validation.constraints.NotNull;
import me.wjz.creeperhub.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {
    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void afterConnectionEstablished(WebSocketSession session ) {
        sessions.add(session);
        redisUtil.addSession(session.getId());//这里会打印东西
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        redisUtil.removeSession(session.getId());
    }
}
