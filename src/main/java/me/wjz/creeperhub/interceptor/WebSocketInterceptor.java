package me.wjz.creeperhub.interceptor;

import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.utils.RedisUtil;
import me.wjz.creeperhub.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class WebSocketInterceptor implements HandshakeInterceptor {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try {
            String token = WebUtil.getToken();
            User user = redisUtil.getUser(token); // 验证token获取用户信息
            if (user == null) return false; // 认证失败拒绝连接
            attributes.put("userId", user.getId()); //用户ID存储在attribute里面，方便后续调用
            return true;
        }catch (Exception e){
            System.out.println("未找到token,websocket握手失败");
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}
