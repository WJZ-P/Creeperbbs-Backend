package me.wjz.creeperhub.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.controller.UserController;
import me.wjz.creeperhub.dto.UserDTO;
import me.wjz.creeperhub.dto.UserModifyDTO;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.Token;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.mapper.UserMapper;
import me.wjz.creeperhub.utils.HashUtil;
import me.wjz.creeperhub.utils.RandomUtil;
import me.wjz.creeperhub.utils.RedisUtil;
import me.wjz.creeperhub.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    private TokenService tokenService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisUtil redisUtil;
    public static final String LOGIN_ATTEMPT_LIMIT = "login_attempt_limit:";
    @Value("${app.login.attempt-limit}")
    private int MAX_LOGIN_ATTEMPTS;
    @Value("${app.login.attempt-limit-expire-time}")
    private long LOGIN_ATTEMPT_EXPIRE_TIME;
    public BloomFilter<Long> userBloomFilter;

    @PostConstruct
    public void init() {
        //初始化布隆过滤器
        List<Long> userIdList = getAllUserIds();
        if (userIdList != null && !userIdList.isEmpty()) {
            userBloomFilter = BloomFilter.create(
                    Funnels.longFunnel(),
                    userIdList.size() + 100,
                    0.01
            );
            for (Long id : userIdList) {
                userBloomFilter.put(id);
            }
        } else System.out.println("UserController布隆过滤器初始化失败，无user数据");
    }

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
        if (!captchaService.verifyCaptcha(code, email)) {
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

        //保存到数据库，注意这里只有过了一次数据库之后才会有id属性，因为是数据库自增的
        user.setId(userMapper.insertUser(user));
        //往布隆过滤器中添加新注册的用户ID
        userBloomFilter.put(user.getId());

        return Result.success("注册成功！", null);
    }

    public Result<Void> sendRegisterEmail(String email) throws MessagingException {
        //校验邮箱，必须使用QQ邮箱
        if (!email.matches("^\\w+([-+.]\\w+)*@qq.com$")) {
            throw new CreeperException(ErrorType.USER_EMAIL_INCORRECT);
        }
        if (userMapper.findByEmail(email) != null)
            throw new CreeperException(ErrorType.USER_EMAIL_REGISTERED);
        //下面的方法内部会进行缓存控制，防止恶意调用
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
                        <p>请将此验证码填写到注册页面，验证码有效期为 3 分钟。</p>
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
    public Result login(User user, HttpServletResponse response) {
        String username = user.getUsername();
        String password = user.getPassword();
        //下面是redis做登录尝试限制
        String redisKey = LOGIN_ATTEMPT_LIMIT + WebUtil.getClientIp();
        long count = redisService.increase(redisKey, 1);//记录数量
        if (count == 1) {
            //说明是第一次尝试登录，加上登录限制的过期时间
            redisService.expire(redisKey, LOGIN_ATTEMPT_EXPIRE_TIME, TimeUnit.SECONDS);
        }
        if (count > MAX_LOGIN_ATTEMPTS) {
            return Result.error(ErrorType.LOGIN_ATTEMPT_EXCEED);
        }

        //校验用户名和密码
        User targetUser = userMapper.findByUsername(username);
        if (targetUser == null || !HashUtil.check(password, targetUser.getPassword())) {
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
        token.setId(UUID.randomUUID().toString());

        //token设置到HTTP-only的cookie中
        Cookie cookie = new Cookie("token", token.getToken());
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);//https传输
        cookie.setMaxAge(60 * 60 * 24 * 30);//30天
        cookie.setPath("/");//作用于整个域名
        response.addCookie(cookie);
        //这里必须设置cookie的最大时间，否则只会存储在内存中作为会话session，重启就会失效！
        //设置了最大时间后会持久化到硬盘里

//        //将token对象存入数据库
//        userMapper.insertToken(token);    感觉没必要存入数据库，单独放redis里就行

        //然后token存入redis中
        tokenService.setToken(token);

        //user也缓存一份到redis中
        redisService.setMap("user:" + targetUser.getId(), targetUser.toMap());
        redisService.expire("user:" + targetUser.getId(), 60 * 60 * 24 * 30, TimeUnit.SECONDS);
        return Result.success("登录成功！", null);
    }

    public Result logout(String token) {
        String senderToken = WebUtil.getToken();
        //先校验请求中的token是否存在，鉴别请求发起者是否为用户
        if (senderToken == null || !tokenService.validateToken(senderToken)) {
            throw new CreeperException(ErrorType.TOKEN_ERROR);
        }
        //从redis中获取该用户的token，方便后续判断
        Token token1 = tokenService.getToken(senderToken);
        Token token2 = tokenService.getToken(token);
        if (token1 == null) throw new CreeperException(ErrorType.TOKEN_ERROR);
        if (token2 == null) throw new CreeperException(ErrorType.TARGET_TOKEN_ERROR);
        //判断token是否匹配
        if (!token1.getUserId().equals(token2.getUserId())) throw new CreeperException(ErrorType.TOKEN_UNMATCHED);
        //从redis中删除该用户的token
        tokenService.deleteToken(token);
        return Result.success("登出成功！", null);
    }

    public Result getUserInfo(Long id) {
        //使用布隆过滤器进行校验
        if (!userBloomFilter.mightContain(id)) {
            System.out.println("布隆过滤器拦截了id=" + id + "的查询");
            throw new CreeperException(ErrorType.USER_NOT_FOUND);
        }

        User user = null;
        //先从redis中查
        Map<Object, Object> map = redisService.getMap("user:" + id);
        if (!map.isEmpty()) {
            user = User.fromMap(map);
            return Result.success("获取用户信息成功！", UserDTO.fromUser(user));
        } else {
            user = userMapper.findById(id);
            if (user == null) throw new CreeperException(ErrorType.USER_NOT_FOUND);
            redisService.setMap("user:" + id, user.toMap());
            redisService.expire("user:" + id, 60 * 60 * 24 * 30, TimeUnit.SECONDS);
            return Result.success("获取用户信息成功！", UserDTO.fromUser(user));
        }

    }

    public List<Long> getAllUserIds() {
        return userMapper.getAllUserIds();
    }

    public Result updateUserInfo(UserModifyDTO userModifyDTO) {
        String token = WebUtil.getToken();
        //token是正常的
        User user = redisUtil.getUser(token);

        //直接校验密码
        if (userModifyDTO.getOriginalPassword() != null && userModifyDTO.getNewPassword() != null) {
            //根据token查找当前用户
            if (!HashUtil.check(userModifyDTO.getOriginalPassword(), user.getPassword())) {
                //输入的旧密码不匹配
                throw new CreeperException(ErrorType.ORIGINAL_PASSWORD_ERROR);
            }
        }

        //更新用户信息
        userModifyDTO.setId(user.getId());
        //修改之前，用户的新密码要记得哈希
        String newHashedPwd= HashUtil.hash(userModifyDTO.getNewPassword());
        userModifyDTO.setNewPassword(newHashedPwd);
        userMapper.updateUserInfo(userModifyDTO);
        //修改后还得更新redis
        user.setUsername(userModifyDTO.getUsername() == null || userModifyDTO.getUsername().isEmpty() ? user.getUsername() : userModifyDTO.getUsername());
        user.setAvatar(userModifyDTO.getAvatar() == null || userModifyDTO.getAvatar().isEmpty() ? user.getAvatar() : userModifyDTO.getAvatar());
        user.setPassword(userModifyDTO.getNewPassword() == null || userModifyDTO.getNewPassword().isEmpty() ? user.getPassword() : newHashedPwd);
        return Result.success("修改用户信息成功！", null);
    }
}