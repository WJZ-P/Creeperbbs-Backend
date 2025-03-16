package me.wjz.creeperhub.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.wjz.creeperhub.entity.Comment;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CommentEvent implements Serializable {
    private Long commentId;
    private Long postId;
    private Long senderUserId;  //评论者ID
    private Long parentCommentId;
    private String content;//内容
    private Long commentTime;

    public CommentEvent(Comment comment) {
        this.commentId = comment.getId();
        this.postId = comment.getPostId();
        this.senderUserId = comment.getUserId();
        this.parentCommentId = comment.getParentCommentId();
        this.content = comment.getContent();
        this.commentTime = comment.getCreateTime();
    }
}
