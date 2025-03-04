package me.wjz.creeperhub.service;

import jakarta.mail.MessagingException;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.mapper.UserMapper;
import me.wjz.creeperhub.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private EmailService emailService;

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
        String hashedPwd= HashUtil.hash(password);
        user.setPassword(hashedPwd);
        user.setEmail(email);
        //保存到数据库
        userMapper.insertUser(user);
        return Result.success("注册成功！", null);
    }

    public Result<Void> sendRegisterEmail(String email) throws MessagingException {
        //校验邮箱，必须使用QQ邮箱
        if (!email.matches("^\\w+([-+.]\\w+)*@qq.com$")) {
            throw new CreeperException(ErrorType.USER_EMAIL_INCORRECT);
        }

        String catpcha = captchaService.generateCaptcha();
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
                </html>""".replace("[%captchaCode%]", catpcha));
        return Result.success("验证码已发送，请注意查收！", null);
    }
}
