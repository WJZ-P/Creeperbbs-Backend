package me.wjz.creeperhub.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.Post;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.mapper.PostMapper;
import me.wjz.creeperhub.utils.RandomUtil;
import me.wjz.creeperhub.utils.RedisUtil;
import me.wjz.creeperhub.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
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
    //    public BloomFilter<Integer> postBloomFilter;
    public static final String POST_RATE = "rate_limit:post:";
    public static final String MAX_POST_NUM = "constant:max_post_id";
    public static final String REDIS_POST_KEY = "post:";
    @Getter
    private int maxPostId;

    @PostConstruct
    public void init() {
        maxPostId = postMapper.getMaxPostId();
        redisService.set(MAX_POST_NUM, maxPostId);
    }

    public Result releasePost(Post post) {//发布帖子
        User user = redisUtil.getUser(WebUtil.getToken());
        //先做速率控制
        String redisKey = POST_RATE + user.getUsername();
        long count = redisService.increase(redisKey, 1);//记录数量
        if (count == 1) //说明是第一次尝试
            redisService.expire(redisKey, POST_RATE_EXPIRE_TIME, TimeUnit.SECONDS);
        else if (count > MAX_POST_RATE) return Result.error(ErrorType.TOO_MANY_REQUESTS);

        //下面准备发布帖子
        post.setUserId(user.getId());
        post.setCreateTime(System.currentTimeMillis());
        postMapper.insertPost(post);

        //更新缓存中帖子数量数据
        maxPostId = (int) redisService.increase(MAX_POST_NUM, 1);
        //更新redis缓存
        redisService.set(MAX_POST_NUM, maxPostId);
        return Result.success("发布帖子成功！", null);
    }

    //getPosts暂时还没有缓存到数据库
    public Result getPosts(Long userId, String sortField, Integer categoryId, int postType, Boolean desc, int page, int size) {
        //过滤掉不存在的分类ID，防止恶意请求攻击。
        if (categoryId != null && categoryId > 10)
            return Result.error(ErrorType.PARAMS_ERROR);
        // 分页参数计算
        int offset = (page - 1) * size;
        // 参数校验
        if (size > 100) throw new IllegalArgumentException("每页最多100条");
        if (desc == null) desc = true;

        List<Post> posts = postMapper.getPost(
                userId,
                categoryId,
                sortField,
                postType,
                desc,
                offset,
                size
        );
        return Result.success("请求成功", posts);
    }

    public Result getPost(Long id) {
        //检查id是否大于最大值
        if (id == null || id > maxPostId) return Result.error(ErrorType.PARAMS_ERROR);
        //先看缓存中有没有帖子
        Post post = redisUtil.getPost(REDIS_POST_KEY + id);
        if (post == null) {
            post = postMapper.getPostById(id);
            //缓存十分钟到redis中
            redisService.set(REDIS_POST_KEY + id, post.toJson());
            redisService.expire(REDIS_POST_KEY + id, 10 + RandomUtil.getRandomNumber(3), TimeUnit.MINUTES);
        }
        return Result.success("请求成功", post);
    }

    public Long getUserIdByPostId(Long postId) {
        return postMapper.getUserIdByPostId(postId);
    }
}
