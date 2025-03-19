package me.wjz.creeperhub.service;

import me.wjz.creeperhub.dto.LikeDTO;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.utils.JsonUtil;
import me.wjz.creeperhub.utils.RedisUtil;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static me.wjz.creeperhub.service.LikeService.LIKE_TOPIC;

@Service
public class SchedulerService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Scheduled(cron = "0 * * * * ?")
    public void scanLikeSet() {//定时扫描redis中的点赞表，批量处理点赞消息
        redisUtil.executeWithLock("like_record_lock", 5, () -> {
            String key = "like_record";
            DefaultRedisScript<List> script = new DefaultRedisScript<>();
            script.setScriptText(
                    "local members = redis.call('SMEMBERS', KEYS[1])\n" +
                            "redis.call('DEL', KEYS[1])\n" +
                            "return members"
            );
            script.setResultType(List.class); // 声明返回类型为 List

            // 执行脚本，返回集合元素
            List<String> LikeDtoSet = redisService.execute(script, Collections.singletonList(key));
            //先判断长度，看看是否为0
            if (LikeDtoSet.isEmpty()) {
                return null;
            }
            System.out.println("点赞待处理列表不为空，发送MQ点赞列表消息。");

            List<LikeDTO> likeDTOList = LikeDtoSet.stream().map(s -> JsonUtil.fromJson(s, LikeDTO.class)).toList();
            //发送rocketmq消息
            Message<String> message = MessageBuilder
                    .withPayload(JsonUtil.toJson(likeDTOList))
                    .setHeader(RocketMQHeaders.KEYS, key)
                    .build();
            rocketMQTemplate.sendMessageInTransaction(
                    LIKE_TOPIC,
                    message,
                    null
            );
            return null;
        });
    }
}

