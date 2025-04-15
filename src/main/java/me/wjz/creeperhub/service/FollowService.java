package me.wjz.creeperhub.service;

import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.entity.Result;
import me.wjz.creeperhub.entity.User;
import me.wjz.creeperhub.exception.CreeperException;
import me.wjz.creeperhub.mapper.FollowMapper;
import me.wjz.creeperhub.utils.UserContent;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class FollowService {
    private FollowMapper followMapper;

    public Result follow(Long id) {
        User user = UserContent.getUser();
        Long userId = user.getId();
        Boolean success;
        try {
            success = followMapper.follow(userId, id);
        } catch (Exception e) {
            throw new CreeperException(ErrorType.OPERATION_ERROR);
        }
        return Result.success("操作成功", success);
    }

    public Result isFollow(Long id) {
        Boolean isFollow = followMapper.isFollow(UserContent.getUser().getId(), id);
        HashMap<String, String> map = new HashMap<>();
        map.put("isFollow", isFollow.toString());
        return Result.success("查询成功", map);
    }
}
