package me.wjz.creeperhub.entity;

import lombok.Data;

@Data
public class Post {
    private Long id;
    private String title;
    private Long userId;
    private String content;
    private Short status;
    private int viewCount;
    private int commentCount;
}
