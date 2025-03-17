package me.wjz.creeperhub.controller;

import me.wjz.creeperhub.entity.CreeperResponseEntity;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.event.CommentEvent;
import me.wjz.creeperhub.utils.WebSocketSessionManager;
import me.wjz.creeperhub.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@RestController
@RequestMapping("/internal")
public class WebSocketForwardController {
    @Autowired
    private WebSocketSessionManager webSocketSessionManager;

    @PostMapping("/forward/comment")
    public CreeperResponseEntity forwardMessage(@RequestBody CommentEvent commentEvent) {
        String sessionId = WebUtil.getWsSession();

        WebSocketSession session = webSocketSessionManager.getSession(sessionId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(commentEvent.toJson()));
                System.out.println("消息转发成功: sessionId=" + sessionId + ", message=" + commentEvent.toJson());
            } catch (Exception e) {
                System.out.println("消息转发失败: sessionId=" + sessionId);
                e.printStackTrace();
            }
        } else {
            System.out.println("会话不存在或已关闭: sessionId=" + sessionId);
        }
        return new CreeperResponseEntity(Result.success("发送成功", commentEvent));
    }
}

