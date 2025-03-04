package me.wjz.creeperhub.service;

import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CaptchaService captchaService;

    public Result<Void> register(String username, String password, String email, String code) {
        //检查用户名是否已存在
        if (userMapper.findByUsername(username) != null) {
            throw new CreeperException(ErrorType.USER_REGISTERED);
        }
        //接着校验这个code是否正确
        if(!captchaService.verifyCaptcha(code)){
            throw new CreeperException(ErrorType.CAPTCHA_INCORRECT);
        }
        //创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        //保存到数据库
        userMapper.insertUser(user);
        return Result.success("注册成功！", null);
    }
}
