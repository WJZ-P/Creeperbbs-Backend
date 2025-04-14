package me.wjz.creeperhub.utils;

import me.wjz.creeperhub.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SimpleRedisLock {
    @Autowired
    private RedisService redisService;

    private static final RedisScript<Long> UNLOCK_LUA = new DefaultRedisScript<>(
            "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                    "return redis.call('del',KEYS[1]) " +
                    "else return 0 end"
    );

    public boolean tryLock(String lockName, long timeoutSec) {
        Boolean success = redisService.setIfAbsent(lockName, Thread.currentThread().getId(), timeoutSec);
        return Boolean.TRUE.equals(success);
    }


    public void unlock(String lockName) {
        //这里要保证查找锁，检索锁的内容以及删除锁这些步骤的原子性，防止ABA问题，也就是本来发现锁是自己的，准备删除，但是锁自己超时释放，其他线程进入，而又返回当前线程，删掉了不属于自己的锁。
        Long result= redisService.execute((DefaultRedisScript<Long>) UNLOCK_LUA, Collections.singletonList(lockName), Thread.currentThread().getId()+"");
        if(result==null||result==0L){
            throw new IllegalStateException("释放锁失败，请检查是否是当前线程的锁");
        }
    }
}
