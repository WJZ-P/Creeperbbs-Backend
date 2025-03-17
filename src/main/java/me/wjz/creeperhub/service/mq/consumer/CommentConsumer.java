package me.wjz.creeperhub.service.mq.consumer;

import me.wjz.creeperhub.event.CommentEvent;
import me.wjz.creeperhub.handler.NotificationWebSocketHandler;
import me.wjz.creeperhub.service.mq.producer.CommentProducer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RocketMQMessageListener(
        topic = CommentProducer.COMMENT_TOPIC,
        consumerGroup = "comment-consumer-group",
        selectorType = SelectorType.TAG,
        selectorExpression = "notify"
)
@Service
public class CommentConsumer implements RocketMQListener<CommentEvent> {
    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;
    @Override
    public void onMessage(CommentEvent commentEvent) {
        //通过webSocket推送消息
        notificationWebSocketHandler.sendCommentNotification(commentEvent);
    }
}
