package me.wjz.creeperhub.service;

import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.Comment;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.event.CommentEvent;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.mapper.PostMapper;
import me.wjz.creeperhub.utils.RedisUtil;
import me.wjz.creeperhub.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class CommentService {
    @Autowired
    private PostService postService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    //对帖子的评论方法要添加进kafka传递给用户
    @Transactional
    public Result sendPostComment(Comment comment) {
        //校验帖子ID
        if (comment.getPostId() == null || comment.getPostId() > postService.getMaxPostId())
            return Result.error(ErrorType.PARAMS_ERROR);

        //发送评论前，comment对象的内容还是不完整的，比如userID要在这里传入
        comment.setUserId(redisUtil.getUser(WebUtil.getToken()).getId());
        comment.setCreateTime(System.currentTimeMillis());
        if (comment.getParentCommentId() == null) comment.setParentCommentId((long) -1);
        //将评论插入到数据库中
        postMapper.insertComment(comment);

        //下面发送kafka事件
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        //事务提交后，发送kafka事件
                        sendCommentEvent(comment);
                    }
                }
        );

        return Result.success("评论成功", null);
    }

    private void sendCommentEvent(Comment comment) {
        //发送kafka事件
        try {
            CommentEvent event = new CommentEvent(
                    comment.getId(),
                    comment.getPostId(),
                    comment.getUserId(),
                    comment.getParentCommentId(),
                    comment.getContent(),
                    System.currentTimeMillis());
            //发送消息到kafka
            kafkaTemplate.send("comment", event);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CreeperException(ErrorType.UNKNOWN_ERROR);
        }
    }
}
