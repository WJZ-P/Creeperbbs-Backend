package me.wjz.creeperhub.entity;

import lombok.Data;

@Data
public class Token {
    private Long id;
    private Long userId;
    private String token;
    private String ipAddress;
    private String deviceInfo;
    private Long createTime;
}
