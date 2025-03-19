package me.wjz.creeperhub.service;

import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.dto.LikeDTO;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.utils.JsonUtil;
import me.wjz.creeperhub.utils.RedisUtil;
import me.wjz.creeperhub.utils.WebUtil;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class LikeService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtil redisUtil;
    private static final String LIKE_KEY_PREFIX = "like:";

    //处理点赞
    @Transactional
    public Result handleLike(LikeDTO likeDTO) {
        //首先完善这里likeDTO的用户id
        User user = redisUtil.getUser(WebUtil.getToken());
        if (user == null) return Result.error(ErrorType.UNKNOWN_ERROR);
        likeDTO.setUserId(user.getId());

        String redisKey = LIKE_KEY_PREFIX + likeDTO.getTargetType() + ":" + likeDTO.getTargetId();
        //like:post:2
        //用set存储点赞的用户ID，原子性保障
        String luaScript = "if redis.call('SADD', KEYS[1], ARGV[1]) == 1 then " +
                "   redis.call('EXPIRE', KEYS[1], 86400) " + // 设置24小时过期
                "   return 1 " +
                "else " +
                "   return 0 " +
                "end";
        //执行lua脚本，返回1表示点赞成功
        Long result = redisService.execute(new DefaultRedisScript<>(luaScript, Long.class),
                List.of(redisKey),
                likeDTO.getUserId().toString());

        if (result == 1) {
            // 发送RocketMQ事务消息（半消息机制防止丢失）
            Message<String> message = MessageBuilder
                    .withPayload(JsonUtil.toJson(likeDTO))
                    .setHeader(RocketMQHeaders.KEYS, redisKey)
                    .build();
            rocketMQTemplate.sendMessageInTransaction(
                    "like-topic",
                    message,
                    null
            );
            return Result.success("点赞成功！", null);
        } else {
            throw new CreeperException(ErrorType.DOUBLE_LIKE);
        }
    }
}
