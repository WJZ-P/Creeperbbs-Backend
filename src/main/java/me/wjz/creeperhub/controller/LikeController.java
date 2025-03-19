package me.wjz.creeperhub.controller;

import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.dto.LikeDTO;
import me.wjz.creeperhub.entity.CreeperResponseEntity;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.service.LikeService;
import me.wjz.creeperhub.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/like")
    public CreeperResponseEntity like(@RequestBody LikeDTO likeDTO) {
        System.out.println(likeDTO);
        if (likeDTO.getIsLike())
            return new CreeperResponseEntity(likeService.handleLike(likeDTO));
        else {
            return new CreeperResponseEntity(Result.error(ErrorType.PARAMS_ERROR));
        }
    }
}
