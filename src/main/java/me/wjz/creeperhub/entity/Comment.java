package me.wjz.creeperhub.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class Comment {
    private Long id;
    private Long postId;
    private Long userId;
    private Long parentCommentId;
    private String content;
    private Integer likeCount;
    private Integer status;//0删除，1正常
    private Long createTime;

    public String toJson(){
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
