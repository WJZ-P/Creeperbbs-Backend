package me.wjz.creeperhub.config;

import me.wjz.creeperhub.interceptor.LoginInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加登录拦截
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns("api/user/register",
                        "api/user/login",
                        "/api/user/send_register_email");
    }
}