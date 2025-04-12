package me.wjz.creeperhub.controller;

import me.wjz.creeperhub.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Controller
public class SeckillController {
    @Autowired
    private SeckillService seckillService;
}
