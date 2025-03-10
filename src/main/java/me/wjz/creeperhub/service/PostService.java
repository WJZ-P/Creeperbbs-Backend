package me.wjz.creeperhub.service;

import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.Post;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.mapper.PostMapper;
import me.wjz.creeperhub.utils.RedisUtil;
import me.wjz.creeperhub.utils.WebUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.concurrent.TimeUnit;

@Service
public class PostService {
    @Autowired
    private RedisService redisService;
    @Value("${app.post.rate-limit}")
    private int MAX_POST_RATE;
    @Value("${app.post.rate-limit-expire-time}")
    private long POST_RATE_EXPIRE_TIME;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PostMapper postMapper;
    public static final String POST_RATE = "rate_limit:post:";

    public Result post(Post post) {//发布帖子
        //先做速率控制
        String redisKey = POST_RATE + WebUtil.getClientIp();
        long count = redisService.increase(redisKey, 1);//记录数量
        if (count == 1) //说明是第一次尝试
            redisService.expire(redisKey, POST_RATE_EXPIRE_TIME, TimeUnit.SECONDS);
        else if (count > MAX_POST_RATE) return Result.error(ErrorType.TOO_MANY_REQUESTS);

        //下面准备发布帖子
        User user = redisUtil.getUser(WebUtil.getToken());
        post.setUserId(user.getId());
        post.setCreateTime(System.currentTimeMillis());
        postMapper.insertPost(post);
        return Result.success("发布帖子成功！", null);
    }
}
