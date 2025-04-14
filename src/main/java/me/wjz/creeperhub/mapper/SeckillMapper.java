package me.wjz.creeperhub.mapper;

import me.wjz.creeperhub.dto.SeckillDTO;
import me.wjz.creeperhub.entity.SeckillActivity;
import me.wjz.creeperhub.entity.SeckillOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SeckillMapper {
    @Select("SELECT * from seckill_activity where id=#{id}")
    SeckillActivity getSeckillActivity(Long id);
    @Update("UPDATE seckill_activity SET stock=stock-1 WHERE id=#{id} AND stock>0")
    boolean deductStock(long id);
    @Insert("INSERT INTO seckill_order(id,user_id, activity_id,order_time) VALUES(#{id},#{userId}, #{activityId},#{orderTime})")
    void insertOrder(SeckillOrder seckillOrder);
}
