package me.wjz.creeperhub.mapper;

import me.wjz.creeperhub.dto.UserModifyDTO;
import me.wjz.creeperhub.entity.Post;
import me.wjz.creeperhub.entity.Token;
import me.wjz.creeperhub.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users (username, password, email,create_time) " +
            "VALUES (#{username}, #{password}, #{email},${createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Long insertUser(User user);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO user_tokens (user_id, token,ip_address,device_info,create_time) " +
            "VALUES (#{userId}, #{token}, #{ipAddress},#{deviceInfo},#{createTime})")
    void insertToken(Token token);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT id FROM users")
    List<Long> getAllUserIds();

    @Select("Select MAX(id) from users")
    Long getMaxId();

    @Update({
            "<script>",
            "UPDATE users",
            "<set>",
            "<if test='username != null'>username = #{username},</if>",
            "<if test='avatar != null'>avatar = #{avatar},</if>",
            "<if test='newPassword != null'>password = #{newPassword},</if>",
            "</set>",
            "WHERE id = #{id}",
            "</script>"
    })
    void updateUserInfo(UserModifyDTO userModifyDTO);

}

