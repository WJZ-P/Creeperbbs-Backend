package me.wjz.creeperhub.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.exception.CreeperException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebUtil {
    public static String getClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes())
                .getRequest();

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getDeviceInfo() {
        HttpServletRequest request = getRequest();
        String userAgent = request.getHeader("User-Agent");
        return (userAgent == null || userAgent.isEmpty()) ? "未知设备" : userAgent;
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取的是请求发起者的token
     *
     * @return
     */
    public static String getToken() {

            //从request里面提取token
            String token = null;
            Cookie[] cookies = getRequest().getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    break;
                }
            }
            return token;
    }

    public static String getWsSession(){
        //从request里面提取token
        String session = null;
        Cookie[] cookies = getRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("ws-session")) {
                session = cookie.getValue();
                break;
            }
        }
        return session;
    }
}