package me.wjz.creeperhub.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token=null;
        Cookie[] cookies=request.getCookies();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("token")){
                token=cookie.getValue();
                break;
            }
        }
        if(token==null){
            throw new CreeperException(Result.error(ErrorType.UN_LOGIN));
        }
        //校验token是否正确
        return true;
    }
}
