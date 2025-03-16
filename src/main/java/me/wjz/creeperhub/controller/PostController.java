package me.wjz.creeperhub.controller;

import me.wjz.creeperhub.entity.Comment;
import me.wjz.creeperhub.entity.CreeperResponseEntity;
import me.wjz.creeperhub.entity.Post;
import me.wjz.creeperhub.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/posts")//发布帖子接口
    public CreeperResponseEntity post(@RequestBody Post post) {
        return new CreeperResponseEntity(postService.releasePost(post));
    }

    @GetMapping("/posts")//获取帖子列表接口
    public CreeperResponseEntity getPost(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "create_time") String sortField,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "1") int postType,
            @RequestParam(defaultValue = "true") Boolean desc,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new CreeperResponseEntity(postService.getPosts(userId,sortField, categoryId, postType, desc, page, size));
    }

    @GetMapping("/post")//获取帖子详情接口 requestParam就是?xxx=xxx&xxx=xxx的形式。
    // post/{id}这种就是 用 @PathVariable
    public CreeperResponseEntity getPost(@RequestParam(required = false) Long id) {
        return new CreeperResponseEntity(postService.getPost(id));
    }

}
