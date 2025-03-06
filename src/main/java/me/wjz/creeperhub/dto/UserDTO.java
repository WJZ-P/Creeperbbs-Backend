package me.wjz.creeperhub.dto;

import lombok.Data;
import me.wjz.creeperhub.entity.User;

/**
 * 数据脱敏
 */
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String avatar;
    private Integer score;
    private Long createTime;

    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setScore(user.getScore());
        userDTO.setCreateTime(user.getCreateTime());
        return userDTO;
    }
}
