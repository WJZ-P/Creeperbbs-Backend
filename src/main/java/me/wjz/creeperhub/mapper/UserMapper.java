package me.wjz.creeperhub.mapper;

import me.wjz.creeperhub.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users (username, password, email,create_time) " +
            "VALUES (#{username}, #{password}, #{email},${createTime})")
    void insertUser(User user);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);
}

