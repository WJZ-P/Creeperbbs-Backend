package me.wjz.creeperhub.service;

import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.dto.SeckillDTO;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.SeckillActivity;
import me.wjz.creeperhub.entity.SeckillOrder;
import me.wjz.creeperhub.mapper.SeckillMapper;
import me.wjz.creeperhub.utils.SimpleRedisLock;
import me.wjz.creeperhub.utils.SnowFlake;
import me.wjz.creeperhub.utils.UserContent;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SeckillService {
    @Autowired
    private SeckillMapper seckillMapper;
    @Autowired
    private SimpleRedisLock redisLock;
    @Autowired
    private SnowFlake snowFlake;

    public Result takeHandler(SeckillDTO seckillDTO) {
        //先查优惠券活动信息
        SeckillActivity seckillActivity = seckillMapper.getSeckillActivity(seckillDTO.getActivityId());
        //判断秒杀活动是否在活动期间
        if (System.currentTimeMillis() < seckillActivity.getStartTime())
            return new Result(ErrorType.NOT_START);
        if (System.currentTimeMillis() > seckillActivity.getEndTime())
            return new Result(ErrorType.ALREADY_END);
        if (seckillActivity.getStock() < 1)
            return new Result(ErrorType.STOCK_NOT_ENOUGH);
        //下面可以开始抢购了

        //为了解决超卖问题，先在Redis里面抢占用户锁。
        boolean isLock = redisLock.tryLock("secKill:" + UserContent.getUser().getId(), 2);
        try{
            if (isLock) {//如果抢到锁了，则进行数据库操作
                if(!seckillMapper.deductStock(seckillDTO.getActivityId()))
                    return new Result(ErrorType.STOCK_NOT_ENOUGH);//库存扣减失败，则返回错误。

                //这里为止说明库存扣减成功了，然后要新建秒杀订单
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setId(snowFlake.nextId());
                seckillOrder.setUserId(UserContent.getUser().getId());
                seckillOrder.setActivityId(seckillDTO.getActivityId());
                seckillOrder.setOrderTime(System.currentTimeMillis());
                //插入订单
                seckillMapper.insertOrder(seckillOrder);
                //完毕，返回订单ID
                return Result.success("抢购成功！", seckillOrder.getId());
            }
        }finally {
            redisLock.unlock("secKill:" + UserContent.getUser().getId());
        }
        //执行到这里说明不对，返回出错。
        return new Result(ErrorType.UNKNOWN_ERROR);
    }
}
