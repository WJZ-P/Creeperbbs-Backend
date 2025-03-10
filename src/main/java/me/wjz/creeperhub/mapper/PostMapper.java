package me.wjz.creeperhub.mapper;

import me.wjz.creeperhub.entity.Post;
import me.wjz.creeperhub.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {
    @Insert("INSERT INTO posts (title, content, user_id,category_id,create_time) " +
            "VALUES (#{title}, #{content}, #{userId},#{categoryId}, #{createTime})")
    void insertPost(Post post);
}
