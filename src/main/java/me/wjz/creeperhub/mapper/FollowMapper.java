package me.wjz.creeperhub.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FollowMapper {
    @Select("select * from follow where user_id=#{userId} and follow_user_id=#{followId}")
    public boolean isFollow(long userId, long followId);
    @Insert("insert into follow (user_id,follow_user_id) values(#{userId},#{followId})")
    public boolean follow(long userId, long followId);
}
