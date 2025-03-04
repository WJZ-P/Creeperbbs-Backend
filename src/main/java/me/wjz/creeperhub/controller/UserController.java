package me.wjz.creeperhub.controller;

import jakarta.mail.MessagingException;
import me.wjz.creeperhub.dto.RegisterDTO;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.service.CaptchaService;
import me.wjz.creeperhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/user/register")//注册接口
    public Result<Void> register(@RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO.getUsername(), registerDTO.getPassword(),
                registerDTO.getEmail(), registerDTO.getCode());
    }

    @GetMapping("/user/getCaptcha")//获取验证码接口,仅是测试用
    public Result<String> getCaptcha() {
        String captcha = captchaService.generateCaptcha();
        return Result.success("验证码获取成功", captcha);
    }

    @GetMapping("/user/sendRegisterEmail")//发送注册邮件接口
    public Result<Void> sendRegisterEmail(@RequestParam String email) throws MessagingException {
        return userService.sendRegisterEmail(email);
    }

    @PostMapping("/user/login")//登录接口
    public Result<Void> login(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getPassword());
    }
}
