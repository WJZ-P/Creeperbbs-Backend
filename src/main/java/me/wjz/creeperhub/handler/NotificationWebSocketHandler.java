package me.wjz.creeperhub.handler;

import jakarta.validation.constraints.NotNull;
import me.wjz.creeperhub.entity.Comment;
import me.wjz.creeperhub.event.CommentEvent;
import me.wjz.creeperhub.utils.RedisUtil;
import me.wjz.creeperhub.utils.WebSocketSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private WebSocketSessionManager webSocketSessionManager;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        webSocketSessionManager.addSession(session);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessionManager.removeSession(session);
    }

    //发送评论通知
    public void sendCommentNotification(CommentEvent commentEvent) {
        try {
            //从redis中获取用户对应的sessionID.
            String sessionId = redisUtil.getSessionId(commentEvent.getReceiverUserId());
            //准备发送消息，看本地缓存是否有这个用户的ID
            if (sessionId == null) return;
            WebSocketSession session = webSocketSessionManager.getSession(sessionId);
            if (session != null) {
                if (!session.isOpen()) return;
                //下面说明这台服务器上有这个用户的session，可以发送消息啦
                session.sendMessage(new TextMessage(commentEvent.toJson()));
            }
            else{
                //如果本地缓存没有，则要转发消息，得从redis中获得有这个sessionID的服务器

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
