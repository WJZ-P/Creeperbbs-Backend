package me.wjz.creeperhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Map<String,Object>> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Map<String,Object>> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // Key 序列化器还是用 StringRedisSerializer 喵！
        template.setKeySerializer(new StringRedisSerializer());

        //  重点！ Value 序列化器换成 GenericJackson2JsonRedisSerializer 喵！
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        //  Hash 的 Key 序列化器也用 StringRedisSerializer 喵！
        template.setHashKeySerializer(new StringRedisSerializer());

        //  Hash 的 Value 序列化器也换成 GenericJackson2JsonRedisSerializer 喵！  这样 Hash 存啥类型的值都行啦喵！
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}
