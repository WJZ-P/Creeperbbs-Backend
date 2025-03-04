package me.wjz.creeperhub.controller;

import me.wjz.creeperhub.dto.RegisterDTO;
import me.wjz.creeperhub.entity.Result;
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
        throw new RuntimeException("嘻嘻哈哈我出错啦");
//            return userService.register(
//                    registerDTO.getUsername(),
//                    registerDTO.getPassword(),
//                    registerDTO.getEmail(),
//                    registerDTO.getCode());
    }

    @GetMapping("/captcha")//获取验证码接口
    public Result<String> getCaptcha() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error(500, "验证码获取失败");
    }
}
