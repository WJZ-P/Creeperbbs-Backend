package me.wjz.creeperhub.controller;

import me.wjz.creeperhub.entity.Comment;
import me.wjz.creeperhub.entity.CreeperResponseEntity;
import me.wjz.creeperhub.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/comment/post")
    public CreeperResponseEntity comment(@RequestBody Comment comment) {
        return new CreeperResponseEntity(commentService.sendPostComment(comment));
    }

}
