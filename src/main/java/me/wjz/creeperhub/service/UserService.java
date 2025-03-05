package me.wjz.creeperhub.service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.Token;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.mapper.UserMapper;
import me.wjz.creeperhub.utils.HashUtil;
import me.wjz.creeperhub.utils.RandomUtil;
import me.wjz.creeperhub.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private TokenService tokenService;
    public static final String LOGIN_ATTEMPT_LIMIT = "login_attempt_limit:";

    @Value("${app.login.attempt-limit}")
    private int MAX_LOGIN_ATTEMPTS;
    @Value("${app.login.attempt-limit-expire-time}")
    private long LOGIN_ATTEMPT_EXPIRE_TIME;

    public Result<Void> register(String username, String password, String email, String code) {
        //检查用户名是否已存在
        if (userMapper.findByUsername(username) != null) {
            throw new CreeperException(ErrorType.USER_REGISTERED);
        }
        //检验用户名是否符合规范
        if (!username.matches("^[a-zA-Z0-9_\\\\u4e00-\\\\u9fa5]{3,16}$")) {
            throw new CreeperException(ErrorType.USERNAME_INCORRECT);
        }
        //检验邮箱是否符合规范
        if (!email.matches("^\\w+([-+.]\\w+)*@qq.com$")) {
            throw new CreeperException(ErrorType.USER_EMAIL_INCORRECT);
        }
        //校验验证码是否正确
        if (!captchaService.verifyCaptcha(code)) {
            throw new CreeperException(ErrorType.CAPTCHA_INCORRECT);
        }
        //校验密码是否符合格式
        if (!password.matches("^[a-zA-Z0-9]{6,16}$")) {
            throw new CreeperException(ErrorType.PASSWORD_INCORRECT);
        }

        //创建新用户
        User user = new User();
        user.setUsername(username);
        //密码加密
        String hashedPwd = HashUtil.hash(password);
        user.setPassword(hashedPwd);
        user.setEmail(email);
        user.setCreateTime(System.currentTimeMillis());
        //保存到数据库
        userMapper.insertUser(user);
        return Result.success("注册成功！", null);
    }

    public Result<Void> sendRegisterEmail(String email) throws MessagingException {
        //校验邮箱，必须使用QQ邮箱
        if (!email.matches("^\\w+([-+.]\\w+)*@qq.com$")) {
            throw new CreeperException(ErrorType.USER_EMAIL_INCORRECT);
        }

        String captcha = captchaService.generateCaptcha(email);
        emailService.sendEmail(email, "欢迎注册苦力怕论坛！", """
                <!DOCTYPE html>
                <html lang="zh-CN">
                <head>
                    <meta charset="UTF-8">
                    <title>欢迎注册苦力怕论坛！</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            color: #333;
                            margin: 0;
                            padding: 20px;
                        }
                        .container {
                            background-color: #fff;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 30px;
                            border-radius: 8px;
                            box-shadow: 0 0 10px rgba(0,0,0,0.1);
                        }
                        h1 {
                            color: #007bff;
                            text-align: center;
                        }
                        p {
                            line-height: 1.6;
                            margin-bottom: 15px;
                        }
                        .verification-code {
                            font-size: 24px;
                            font-weight: bold;
                            color: #ff6f61;
                            text-align: center;
                            margin-top: 20px;
                            margin-bottom: 20px;
                            letter-spacing: 5px; /* 增加字符间距 */
                        }
                        .button {
                            display: inline-block;
                            padding: 12px 24px;
                            background-color: #007bff;
                            color: #fff;
                            text-decoration: none;
                            border-radius: 5px;
                            font-weight: bold;
                        }
                        .footer {
                            margin-top: 30px;
                            text-align: center;
                            color: #777;
                            font-size: 0.9em;
                        }
                        .logo {
                            display: block; /* 确保 logo 独占一行 */
                            max-width: 150px; /* 控制 logo 最大宽度 */
                            margin: 0 auto 20px; /* 居中并添加下边距 */
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <img src="YOUR_LOGO_URL_HERE" alt="苦力怕论坛 Logo" class="logo">  <h1>欢迎加入苦力怕论坛！</h1>
                        <p>亲爱的用户，</p>
                        <p>感谢您注册苦力怕论坛！为了完成注册，请使用以下验证码进行邮箱验证：</p>
                        <p class="verification-code"> [%captchaCode%]  </p>
                        <p>请将此验证码填写到注册页面，验证码有效期为 1 分钟。</p>
                        <p>如果您没有进行注册操作，请忽略此邮件。请勿将验证码泄露给他人。</p>
                        <p>期待您在苦力怕论坛的精彩旅程！</p>
                        <p class="footer">
                            此致，<br>
                            苦力怕论坛 WJZ_P<br>
                            <a href="www.CreeperHub.com" style="color: #007bff; text-decoration: none;">[www.CreeperHub.com]</a> </p>
                    </div>
                </body>
                </html>""".replace("[%captchaCode%]", captcha));
        return Result.success("验证码已发送，请注意查收！", null);
    }

    //登录
    @Transactional
    public Result login(User user, HttpServletResponse response) {
        String username = user.getUsername();
        String password = user.getPassword();
        //下面是redis做登录尝试限制
        String redisKey = LOGIN_ATTEMPT_LIMIT + WebUtil.getClientIp();
        long count = redisTemplate.opsForValue().increment(redisKey, 1);//记录数量
        if (count == 1) {
            //说明是第一次尝试登录，加上登录限制的过期时间
            redisTemplate.expire(redisKey, LOGIN_ATTEMPT_EXPIRE_TIME, TimeUnit.SECONDS);
        }
        if (count > MAX_LOGIN_ATTEMPTS) {
            return Result.error(ErrorType.LOGIN_ATTEMPT_EXCEED);
        }

        //校验用户名和密码
        User targetUser = userMapper.findByUsername(username);
        if (targetUser == null || !targetUser.getPassword().equals(HashUtil.hash(password))) {
            Map<String, Integer> map = new HashMap<>();
            map.put("restAttempts", MAX_LOGIN_ATTEMPTS - (int) count);
            return Result.error(ErrorType.LOGIN_PARAMS_ERROR, map);
        }

        //下面完成成功登录后的后续操作

        //先设置好token对象的属性
        Token token = new Token();
        token.setToken(RandomUtil.getRandomString(32));
        token.setUserId(targetUser.getId());
        token.setCreateTime(System.currentTimeMillis());
        token.setIpAddress(WebUtil.getClientIp());
        token.setDeviceInfo(WebUtil.getDeviceInfo());

        //token设置到HTTP-only的cookie中
        Cookie cookie = new Cookie("token", token.getToken());
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);//https传输
        cookie.setMaxAge(60 * 60 * 24 * 30);//30天
        cookie.setPath("/");//作用于整个域名
        response.addCookie(cookie);
        //这里必须设置cookie的最大时间，否则只会存储在内存中作为会话session，重启就会失效！
        //设置了最大时间后会持久化到硬盘里

        //将token对象存入数据库
        userMapper.insertToken(token);
        //然后token存入redis中
        tokenService.setToken(token);
        //user也缓存一份到redis中
        redisTemplate.opsForValue().set("user:" + targetUser.getUsername(), targetUser.toString(), 60 * 60 * 24 * 30, TimeUnit.SECONDS);
        return Result.success("登录成功！", null);
    }
}