package me.wjz.creeperhub.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return super.toString();
        }
    }
}
