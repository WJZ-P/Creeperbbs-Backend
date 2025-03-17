package me.wjz.creeperhub.handler;

import me.wjz.creeperhub.event.CommentEvent;
import me.wjz.creeperhub.utils.RedisUtil;
import me.wjz.creeperhub.utils.WebSocketSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

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
                String serverIp = redisUtil.getServerIpBySessionId(sessionId);

                // 准备请求头，设置 Cookie
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.COOKIE, "ws-session=" + sessionId);
                // 准备请求体
                Map<String, String> requestBody = new HashMap<>();
                //这里实际上是要修改的，不能这么写，因为接口接收的是CommentEvent对象。
                requestBody.put("commentEvent", commentEvent.toJson());
                // 封装请求
                HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
                // 发送请求
                RestTemplate restTemplate=new RestTemplate();
                restTemplate.exchange("http://"+serverIp+"/internal/forward/comment", HttpMethod.POST, requestEntity, Void.class);
                System.out.println("转发评论推送消息到服务器"+serverIp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
