package me.wjz.creeperhub.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.wjz.creeperhub.entity.Comment;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CommentEvent implements Serializable {
    private Long commentId;//前端定位评论位置
    private Long postId;//用来给前端导航到对应帖子
    private Long senderUserId;  //评论者ID
    private Long receiverUserId; //被评论者ID
    private Long parentCommentId;//父评论ID，同样用于导航
    private String content;//直接显示评论内容
    private Long commentTime;//评论时间

    public CommentEvent(Comment comment) {
        this.commentId = comment.getId();
        this.postId = comment.getPostId();
        this.senderUserId = comment.getUserId();
        this.parentCommentId = comment.getParentCommentId();
        this.content = comment.getContent();
        this.commentTime = comment.getCreateTime();
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
