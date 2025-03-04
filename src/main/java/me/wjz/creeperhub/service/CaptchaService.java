package me.wjz.creeperhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CaptchaService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_KEY_PREFIX = "captcha:";
    private static final String RATE_LIMIT_PREFIX = "rate_limit:";
    private static final int CAPTCHA_LENGTH = 6;//验证码长度
    private static final int RATE_LIMIT_TIME_WINDOW = 60;//速率限制时间窗口（秒）
    private static final int RATE_LIMIT_MAX_REQUESTS = 2;//速率限制最大请求数

    //生成验证码
//    public String generateCaptcha() {
//        String ip=getClientIp();
//
//        if(isRateLimited(ip)){
//            throw new RuntimeException("请求过于频繁，请稍后再试");
//        }
//    }
}
