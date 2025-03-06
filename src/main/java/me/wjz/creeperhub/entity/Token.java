package me.wjz.creeperhub.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Token {
    private String id;
    private Long userId;
    private String token;
    private String ipAddress;
    private String deviceInfo;
    private Long createTime;

    public static Token fromMap(Map<Object, Object> map) {
        Token token = new Token();
        Object idObj = map.get("id");
        if (idObj != null) token.setId(String.valueOf(idObj));
        else token.setId(null);

        Object userIdObj = map.get("userId");
        if (userIdObj != null) token.setUserId(((Integer) userIdObj).longValue());
        else token.setUserId(null);

        token.setToken((String) map.get("token"));
        token.setIpAddress((String) map.get("ipAddress"));
        token.setDeviceInfo((String) map.get("deviceInfo"));

        //  安全地获取 createTime，如果 map 中 createTime 为 null，则 token.setCreateTime(null)
        Object createTimeObj = map.get("createTime");
        if (createTimeObj != null) {
            token.setCreateTime((Long) createTimeObj);
        } else {
            token.setCreateTime(null);
        }
        return token;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return super.toString();
        }
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", userId);
        map.put("token", token);
        map.put("ipAddress", ipAddress);
        map.put("deviceInfo", deviceInfo);
        map.put("createTime", createTime);
        return map;
    }
}
