package me.wjz.creeperhub.dto;

import lombok.Data;

@Data
public class LikeDTO {
    private Long userId;//点赞发起者ID
    private String targetType;//post/comment
    private Long targetId;
    private Long createTime;
    private Boolean isLike;//true为点赞，false为取消点赞
}
