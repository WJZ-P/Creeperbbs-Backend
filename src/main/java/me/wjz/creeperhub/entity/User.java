package me.wjz.creeperhub.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

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
    private Integer isBan;

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

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", username);
        map.put("password", password);
        map.put("email", email);
        map.put("avatar", avatar);
        map.put("score", score);
        map.put("createTime", createTime);
        map.put("updateTime", updateTime);
        map.put("isBan", isBan);
        return map;
    }
}
