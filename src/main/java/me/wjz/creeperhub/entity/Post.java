package me.wjz.creeperhub.entity;

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
}
