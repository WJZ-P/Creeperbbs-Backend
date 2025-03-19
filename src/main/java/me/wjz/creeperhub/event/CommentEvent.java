package me.wjz.creeperhub.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.wjz.creeperhub.entity.Comment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class CommentEvent implements Serializable {
    private Long commentId;//前端定位评论位置
    private Long postId;//用来给前端导航到对应帖子
    private Long senderUserId;  //评论者ID
    private Long targetUserId; //被评论者ID
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
        this.targetUserId = comment.getTargetUserId();
    }

    public CommentEvent() {
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

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("commentId", String.valueOf(commentId));
        map.put("postId", String.valueOf(postId));
        map.put("senderUserId", String.valueOf(senderUserId));
        map.put("targetUserId", String.valueOf(targetUserId));
        map.put("parentCommentId", String.valueOf(parentCommentId));
        map.put("content", content);
        map.put("commentTime", String.valueOf(commentTime));
        return map;
    }

    public static CommentEvent fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, CommentEvent.class);
        } catch (JsonProcessingException e) {
            System.out.println("CommentEvent转换失败，不抛出异常，正常执行" + json);
        }
        return new CommentEvent();
    }
}
