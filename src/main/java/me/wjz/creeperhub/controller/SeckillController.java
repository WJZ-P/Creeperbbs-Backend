package me.wjz.creeperhub.controller;

import me.wjz.creeperhub.dto.SeckillDTO;
import me.wjz.creeperhub.entity.CreeperResponseEntity;
import me.wjz.creeperhub.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/seckill")
public class SeckillController {
    @Autowired
    private SeckillService seckillService;

    @PostMapping("/take")
    public CreeperResponseEntity take(@RequestBody SeckillDTO seckillDTO){
        //这里可以用布隆过滤器做限流处理，但先不做处理
        return new CreeperResponseEntity(seckillService.takeHandler(seckillDTO));
    }
}
