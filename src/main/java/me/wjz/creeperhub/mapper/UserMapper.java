package me.wjz.creeperhub.mapper;

import me.wjz.creeperhub.entity.Token;
import me.wjz.creeperhub.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users (username, password, email,create_time) " +
            "VALUES (#{username}, #{password}, #{email},${createTime})")
    void insertUser(User user);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);
    @Insert("INSERT INTO user_tokens (user_id, token,ip_address,device_info,create_time) " +
            "VALUES (#{userId}, #{token}, #{ipAddress},#{deviceInfo},#{createTime})")
    void insertToken(Token token);

    List<String> findTokenByUserId(int userId);
}

