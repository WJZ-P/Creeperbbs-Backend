package me.wjz.creeperhub.service.mq.listener;

import apache.rocketmq.v2.Message;
import me.wjz.creeperhub.dto.LikeDTO;
import me.wjz.creeperhub.service.RedisService;
import me.wjz.creeperhub.utils.JsonUtil;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.StandardCharsets;

@RocketMQTransactionListener
public class LikeTransactionListener implements RocketMQLocalTransactionListener {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(org.springframework.messaging.Message message, Object o) {
        // 半消息发送成功后的本地事务检查
        try {
            // 这里可以加入本地事务日志记录等操作
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    //当 executeLocalTransaction 方法返回 UNKNOW 时，RocketMQ Broker 会稍后定期地调用这个方法来询问本地事务的状态。
    //当生产者在 executeLocalTransaction 方法执行完毕后，由于网络原因或者其他异常导致未能及时向 RocketMQ Broker 发送 Commit 或 Rollback 指令时，RocketMQ Broker 会认为该事务的状态不确定，并会定期地调用这个方法来确认本地事务的状态。
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(org.springframework.messaging.Message message) {
        return RocketMQLocalTransactionState.COMMIT;//这里不判断直接提交一下吧
    }
}
