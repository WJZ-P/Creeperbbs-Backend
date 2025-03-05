package me.wjz.creeperhub.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class CaptchaService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String CAPTCHA_PREFIX = "captcha:";
    private static final String RATE_LIMIT_PREFIX = "captcha_rate_limit:";
    @Value("${app.register.captcha-length}")
    private int CAPTCHA_LENGTH;//验证码长度
    @Value("${app.register.get-captcha-limit-expire-time}")
    private int RATE_LIMIT_TIME_WINDOW;//速率限制时间窗口
    @Value("${app.register.get-captcha-limit}")
    private int RATE_LIMIT_MAX_REQUESTS;//一定时间内获取验证码的最大次数
    @Value("${app.register.captcha-expire-time}")
    private int CAPTCHA_TIME_OUT;

    //生成验证码
    public String generateCaptcha(String emailAddress) {
        String ip = WebUtil.getClientIp();

        if (isRateLimited(ip)) {
            throw new CreeperException(ErrorType.CAPTCHA_REQUEST_LIMIT_EXCEEDED);
        }

        //生成随机验证码
        String captcha = generateRandomCaptcha();
        //将验证码存入redis,60秒有效期
        String key = CAPTCHA_PREFIX + ip + ":" + emailAddress;
        redisTemplate.opsForValue().set(key, captcha, CAPTCHA_TIME_OUT, TimeUnit.SECONDS);
        return captcha;
    }

    public boolean verifyCaptcha(String captcha) {
        String ip = WebUtil.getClientIp();
        String key = CAPTCHA_PREFIX + ip;
        String storedCaptcha = redisTemplate.opsForValue().get(key);
        if (storedCaptcha == null) {
            throw new CreeperException(ErrorType.CAPTCHA_NOT_FOUND);
        }
        return captcha.equals(storedCaptcha);
    }

    private String generateRandomCaptcha() {
        String chars = "0123456789";
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
