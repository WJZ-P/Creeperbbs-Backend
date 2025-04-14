package me.wjz.creeperhub.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.Token;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.service.RedisService;
import me.wjz.creeperhub.service.TokenService;
import me.wjz.creeperhub.utils.RedisUtil;
import me.wjz.creeperhub.utils.UserContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("登录拦截器开始校验");
        String token = null;
        Cookie[] cookies = request.getCookies();
        if(cookies==null) throw new CreeperException(Result.error(ErrorType.UN_LOGIN));
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
                break;
            }
        }
        //校验token
        if (token == null || token.isEmpty() || !tokenService.validateToken(token)) {
            throw new CreeperException(Result.error(ErrorType.UN_LOGIN));
        }
        //更新token有效期
        redisService.expire(TokenService.TOKEN_PREFIX + token, 60 * 60 * 24 * 30, TimeUnit.SECONDS);
        //用户信息存放在ThreadLocal内部，方便后续调用

        User user = redisUtil.getUser(token);
        UserContent.setUser(user);
        //用户存在，放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //清除ThreadLocal中的用户信息,防止泄露内存
        UserContent.remove();
    }
}
