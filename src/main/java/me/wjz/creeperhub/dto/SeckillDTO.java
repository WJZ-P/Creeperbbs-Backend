package me.wjz.creeperhub.dto;

import lombok.Data;

@Data
public class SeckillDTO {
    Long activityId;//优惠券所属活动ID
    //Long userId;//申请抢优惠券的用户ID,实际上也不需要，因为有上下文，谁登录了id就是谁的
}
