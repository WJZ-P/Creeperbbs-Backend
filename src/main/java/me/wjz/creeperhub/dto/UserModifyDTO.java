package me.wjz.creeperhub.dto;

import lombok.Data;

@Data
public class UserModifyDTO {
    //存放可以修改的用户字段
    private String username;
    private String avatar;
    private String originalPassword;
    private String newPassword;
}


