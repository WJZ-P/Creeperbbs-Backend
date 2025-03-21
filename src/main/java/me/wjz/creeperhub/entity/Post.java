package me.wjz.creeperhub.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class Post {
    private Long id;
    private Long userId;
    private int categoryId;
    private String title;
    private String content;
    private int status;
    private int viewCount;
    private int commentCount;
    private int likeCount;
    private int isDeleted;
    private Long createTime;
    private Long updateTime;

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
