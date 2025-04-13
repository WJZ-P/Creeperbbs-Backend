package me.wjz.creeperhub.mapper;

import me.wjz.creeperhub.dto.SeckillDTO;
import me.wjz.creeperhub.entity.SeckillActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SeckillMapper {
    @Select("SELECT * from seckill_activity where id=#{id}")
    SeckillActivity getSeckillActivity(Long id);
}
