package me.wjz.creeperhub.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import me.wjz.creeperhub.dto.RegisterDTO;
import me.wjz.creeperhub.entity.CreeperResponseEntity;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.service.CaptchaService;
import me.wjz.creeperhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/user/register")//注册接口
    public CreeperResponseEntity register(@RequestBody RegisterDTO registerDTO) {
        Result<Void> result = userService.register(registerDTO.getUsername(), registerDTO.getPassword(),
                registerDTO.getEmail(), registerDTO.getCode());
        return new CreeperResponseEntity(result);
    }

    @GetMapping("/user/get_captcha")//获取验证码接口,仅是测试用
    public CreeperResponseEntity getCaptcha() {
        String captcha = captchaService.generateCaptcha();
        // 创建 HashMap 用于封装验证码数据
        HashMap<String, Object> captchaData = new HashMap<>();
        captchaData.put("captcha", captcha);
        return new CreeperResponseEntity(Result.success("验证码获取成功", captchaData));
    }

    @GetMapping("/user/send_register_email")//发送注册邮件接口
    public CreeperResponseEntity sendRegisterEmail(@RequestParam String email) throws MessagingException {
        return new CreeperResponseEntity(userService.sendRegisterEmail(email));
    }

    @PostMapping("/user/login")//登录接口
    public CreeperResponseEntity login(@RequestBody User user, HttpServletResponse response) {
        return new CreeperResponseEntity(userService.login(user,response));
    }
}
