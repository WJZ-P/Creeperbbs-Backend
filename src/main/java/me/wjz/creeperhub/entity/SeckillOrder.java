package me.wjz.creeperhub.entity;

import lombok.Data;

@Data
public class SeckillOrder {
    Long id;
    Long userId;
    Long activityId;
    Long orderTime;
}
