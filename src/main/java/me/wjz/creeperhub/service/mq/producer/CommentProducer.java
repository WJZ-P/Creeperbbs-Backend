package me.wjz.creeperhub.service.mq.producer;

import me.wjz.creeperhub.entity.Comment;
import me.wjz.creeperhub.event.CommentEvent;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class CommentProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    public static final String COMMENT_TOPIC = "comment-topic";

    public void sendCommentEvent(Comment comment) {
        //这个commentEvent就是发给前端的消息了
        CommentEvent commentEvent = new CommentEvent(comment);
        System.out.println("生产者发送评论事件"+commentEvent.toJson());
        rocketMQTemplate.send(COMMENT_TOPIC, MessageBuilder.withPayload(commentEvent.toJson())
                .setHeader(RocketMQHeaders.KEYS, comment.getId()).build());
    }
}
