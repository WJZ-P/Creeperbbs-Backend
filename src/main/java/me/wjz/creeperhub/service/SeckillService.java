package me.wjz.creeperhub.service;

import me.wjz.creeperhub.dto.SeckillDTO;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.SeckillActivity;
import me.wjz.creeperhub.mapper.SeckillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SeckillService {
    @Autowired
    private SeckillMapper seckillMapper;
    public Result takeHandler(SeckillDTO seckillDTO){
        //先查优惠券活动信息
        SeckillActivity seckillActivity= seckillMapper.getSeckillActivity(seckillDTO.getActivityId());

    }
}
