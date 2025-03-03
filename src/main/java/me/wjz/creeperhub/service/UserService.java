package me.wjz.creeperhub.service;

import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User register(String username, String password, String email) {
        if (userMapper.findByUsername(username) != null) {
            throw new RuntimeException("用户名已存在");
        }
        //创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        //保存到数据库
        userMapper.insertUser(user);
        return user;
    }
}
