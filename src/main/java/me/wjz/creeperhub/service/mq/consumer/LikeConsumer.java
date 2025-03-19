package me.wjz.creeperhub.service.mq.consumer;

import me.wjz.creeperhub.dto.LikeDTO;
import me.wjz.creeperhub.mapper.LikeMapper;
import me.wjz.creeperhub.utils.JsonUtil;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static me.wjz.creeperhub.service.LikeService.LIKE_TOPIC;

@RocketMQMessageListener(
        topic = LIKE_TOPIC,
        consumerGroup = "like-consumer-group"
//        selectorType = SelectorType.TAG,
//        selectorExpression = "notify"
)
@Service
public class LikeConsumer implements RocketMQListener<String> {
    @Autowired
    private LikeMapper likeMapper;

    //批量处理redis中的点赞消息。
    @Override
    public void onMessage(String message) {
        System.out.println("收到点赞列表消息：" + message);
        List<LikeDTO> likeDTOList = JsonUtil.fromJson(message, List.class);
        //收到消息后写入数据库
        likeMapper.batchInsert(likeDTOList);
    }

}
