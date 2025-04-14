package me.wjz.creeperhub.config;

import me.wjz.creeperhub.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加登录拦截
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/api/user/register",
                        "/api/user/login",
                        "/api/user/send_register_email",
                        "/ws/notifications",
                        "/ws/**");
    }
}