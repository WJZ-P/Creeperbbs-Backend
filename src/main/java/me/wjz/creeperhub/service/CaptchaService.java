package me.wjz.creeperhub.service;

import jakarta.servlet.http.HttpServletRequest;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.exception.CreeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String CAPTCHA_PREFIX = "captcha:";
    private static final String RATE_LIMIT_PREFIX = "captcha_rate_limit:";
    private static final int CAPTCHA_LENGTH = 6;//验证码长度
    private static final int RATE_LIMIT_TIME_WINDOW = 60;//速率限制时间窗口（秒）
    private static final int RATE_LIMIT_MAX_REQUESTS = 2;//速率限制最大请求数

    //生成验证码
    public String generateCaptcha() {
        String ip = getClientIp();

        if (isRateLimited(ip)) {
            throw new CreeperException(ErrorType.CAPTCHA_REQUEST_LIMIT_EXCEEDED);
        }

        //生成随机验证码
        String captcha = generateRandomCaptcha();
        //将验证码存入redis,60秒有效期
        String key = CAPTCHA_PREFIX + ip;
        redisTemplate.opsForValue().set(key, captcha, 60, TimeUnit.SECONDS);
        return captcha;
    }

    public boolean verifyCaptcha(String captcha) {
        String ip = getClientIp();
        String key = CAPTCHA_PREFIX + ip;
        String storedCaptcha = redisTemplate.opsForValue().get(key);
        if (storedCaptcha == null) {
            throw new CreeperException(ErrorType.CAPTCHA_NOT_FOUND);
        }
        return captcha.equals(storedCaptcha);
    }

    private String getClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes())
                .getRequest();

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    private String generateRandomCaptcha(){
        String chars="0123456789";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            captcha.append(chars.charAt(random.nextInt(chars.length())));
        }
        return captcha.toString();
    }
    //验证码生成速率限制
    private boolean isRateLimited(String ip) {
        String key = RATE_LIMIT_PREFIX + ip;
        long count = redisTemplate.opsForValue().increment(key, 1);//这个操作本身是原子性的
        if (count == 1) {
            //第一次请求，设置过期时间
            redisTemplate.expire(key, RATE_LIMIT_TIME_WINDOW, TimeUnit.SECONDS);
        }
        return count > RATE_LIMIT_MAX_REQUESTS;
    }
}
