package me.wjz.creeperhub.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import me.wjz.creeperhub.dto.RegisterDTO;
import me.wjz.creeperhub.dto.UserModifyDTO;
import me.wjz.creeperhub.entity.CreeperResponseEntity;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.Token;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user/register")//注册接口
    public CreeperResponseEntity register(@RequestBody RegisterDTO registerDTO) {
        Result<Void> result = userService.register(registerDTO.getUsername(), registerDTO.getPassword(),
                registerDTO.getEmail(), registerDTO.getCode());
        return new CreeperResponseEntity(result);
    }

//    @GetMapping("/user/get_captcha")//获取验证码接口,仅是测试用
//    public CreeperResponseEntity getCaptcha(String email) {
//        String captcha = captchaService.generateCaptcha(email);
//        return new CreeperResponseEntity(Result.success("验证码获取成功", captcha));
//    }

    @GetMapping("/user/register_email")//发送注册邮件接口
    public CreeperResponseEntity sendRegisterEmail(@RequestParam String email) throws MessagingException {
        return new CreeperResponseEntity(userService.sendRegisterEmail(email));
    }

    @PostMapping("/user/login")//登录接口
    public CreeperResponseEntity login(@RequestBody User user, HttpServletResponse response) {
        return new CreeperResponseEntity(userService.login(user, response));
    }

    @PostMapping("/user/logout")//登出接口
    public CreeperResponseEntity logout(@RequestBody Token token) {
        return new CreeperResponseEntity(userService.logout(token.getToken()));
    }

    @GetMapping("/user/profile")//获取用户信息接口
    public CreeperResponseEntity getUserInfo(@RequestParam Long id) {
        return new CreeperResponseEntity(userService.getUserInfo(id));
    }

    @PutMapping("/user/profile")//修改用户信息接口
    public CreeperResponseEntity updateUserInfo(@RequestBody UserModifyDTO userModifyDTO) {
        return new CreeperResponseEntity(userService.updateUserInfo(userModifyDTO));
    }
}
