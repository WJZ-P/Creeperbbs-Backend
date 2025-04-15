package me.wjz.creeperhub.controller;

import me.wjz.creeperhub.entity.CreeperResponseEntity;
import me.wjz.creeperhub.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/follow")
public class FollowController {
    @Autowired
    private FollowService followService;
    //关注接口

    @PutMapping("/{id}")
    public CreeperResponseEntity follow(@PathVariable Long id) {
        return new CreeperResponseEntity(followService.follow(id));
    }
    @GetMapping("isfollow/{id}")
    public CreeperResponseEntity isFollow(@PathVariable Long id){
        return new CreeperResponseEntity(followService.isFollow(id));
    }
}
