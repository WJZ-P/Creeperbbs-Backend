package me.wjz.creeperhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String,String> stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Map<String,Object>> mapRedisTemplate;
    @Autowired
    private RedisTemplate<String, Long> longRedisTemplate;
    @Autowired
    private RedisTemplate<String, Integer> integerRedisTemplate;
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key,value);
    }
    public void set(String key, Integer value) {
        integerRedisTemplate.opsForValue().set(key,value);
    }
    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
    public Integer getInteger(String key) {
        return integerRedisTemplate.opsForValue().get(key);
    }
    public Map<Object, Object> getMap(String key) {
        return mapRedisTemplate.opsForHash().entries(key);
    }
    public void setMap(String key, Map<String,?> map) {mapRedisTemplate.opsForHash().putAll(key,map);}
    public void delete(String key) { stringRedisTemplate.delete(key);}
    public boolean hasKey(String key) {return stringRedisTemplate.hasKey(key);}
    public long increase(String key,int delta) {
        return stringRedisTemplate.opsForValue().increment(key,delta);
    }
    public void expire(String key, long amount, TimeUnit timeUnit) {
        stringRedisTemplate.expire(key,amount,timeUnit);
    }
    public boolean setIfAbsent(String key, long value,long expireSecond) {
        return longRedisTemplate.opsForValue().setIfAbsent(key,value,expireSecond,TimeUnit.SECONDS);
    }
    public void addSet(String key, String value) {
        stringRedisTemplate.opsForSet().add(key,value);
    }
    public void deleteSet(String key, String value) {
        stringRedisTemplate.opsForSet().remove(key,value);
    }
}
