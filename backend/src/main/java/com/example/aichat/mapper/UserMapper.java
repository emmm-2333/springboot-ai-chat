package com.example.aichat.mapper;

import com.example.aichat.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username} LIMIT 1")
    User findByUsername(String username);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM user ORDER BY id DESC LIMIT #{offset}, #{size}")
    List<User> findPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(1) FROM user")
    long count();

    @Insert("INSERT INTO user(username,password,email,nickname,avatar,status) VALUES(#{username},#{password},#{email},#{nickname},#{avatar},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE user SET email=#{email}, nickname=#{nickname}, avatar=#{avatar}, status=#{status} WHERE id=#{id}")
    int update(User user);

    @Update("UPDATE user SET password=#{password} WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Delete("DELETE FROM user WHERE id = #{id}")
    int delete(Long id);
}
