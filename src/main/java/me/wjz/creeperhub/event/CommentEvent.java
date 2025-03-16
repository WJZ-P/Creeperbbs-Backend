package me.wjz.creeperhub.event;

import lombok.AllArgsConstructor;
import lombok.Data;

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
}
