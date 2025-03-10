package me.wjz.creeperhub.controller;

import me.wjz.creeperhub.entity.CreeperResponseEntity;
import me.wjz.creeperhub.entity.Post;
import me.wjz.creeperhub.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/posts")//发布帖子接口
    public CreeperResponseEntity post(@RequestBody Post post) {
        return new CreeperResponseEntity(postService.post(post));
    }

}
