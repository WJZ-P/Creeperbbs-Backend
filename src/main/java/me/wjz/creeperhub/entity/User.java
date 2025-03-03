package me.wjz.creeperhub.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private Integer score;
    private Long createTime;
    private Long updateTime;
    private Boolean isBan;
}
