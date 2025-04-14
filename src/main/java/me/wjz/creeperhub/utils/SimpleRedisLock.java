package me.wjz.creeperhub.utils;

import me.wjz.creeperhub.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class SimpleRedisLock {
    @Autowired
    private RedisService redisService;
    public boolean tryLock(String lockName,long timeoutSec){
        String id= Thread.currentThread().getId() + "";
        Boolean success = redisService.setIfAbsent(lockName+":"+id, 1, timeoutSec);
        return Boolean.TRUE.equals(success);
    }

    public void unlock(String lockName) {
        redisService.delete(lockName);
    }
}
