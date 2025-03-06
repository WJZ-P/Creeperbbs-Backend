package me.wjz.creeperhub.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Token {
    private Long id;
    private Long userId;
    private String token;
    private String ipAddress;
    private String deviceInfo;
    private Long createTime;
    private Long refreshTime;

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
        HashMap <String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", userId);
        map.put("token", token);
        map.put("ipAddress", ipAddress);
        map.put("deviceInfo", deviceInfo);
        map.put("createTime", createTime);
        map.put("refreshTime", refreshTime);
        return map;
    }
}
