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
    private String title;
    private Integer permission;

    public static User fromMap(Map<Object, Object> map) {
        User user = new User();
        Object idObj = map.get("id");
        if (idObj != null) user.setId(((Number) idObj).longValue());
        Object scoreObj = map.get("score");
        if (scoreObj != null) user.setScore(((Number) scoreObj).intValue());
        Object createTimeObj = map.get("createTime");
        if (createTimeObj != null) user.setCreateTime(((Number) createTimeObj).longValue());
        Object updateTimeObj = map.get("updateTime");
        if (updateTimeObj != null) user.setUpdateTime(((Number) updateTimeObj).longValue());
        Object isBanObj = map.get("isBan");
        if (isBanObj != null) user.setIsBan(((Number) isBanObj).intValue());
        user.setUsername(map.get("username") != null ? (String) map.get("username") : null);
        user.setPassword(map.get("password") != null ? (String) map.get("password") : null);
        user.setEmail(map.get("email") != null ? (String) map.get("email") : null);
        user.setAvatar(map.get("avatar") != null ? (String) map.get("avatar") : null);
        return user;
    }

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
