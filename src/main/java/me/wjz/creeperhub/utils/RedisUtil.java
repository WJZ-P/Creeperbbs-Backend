package me.wjz.creeperhub.utils;

import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RedisUtil {
    @Autowired
    private RedisService redisService;

    public  User getUser(String token){
        Map<Object,Object> map= redisService.getMap("user:"+token);
        return User.fromMap(map);
    }
}
