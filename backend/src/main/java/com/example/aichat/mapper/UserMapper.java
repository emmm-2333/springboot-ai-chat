package com.example.aichat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.aichat.entity.User;

@Mapper // 标注为 MyBatis 映射接口
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username} LIMIT 1")
    User findByUsername(String username); // 根据用户名查找用户

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id); // 根据用户 ID 查找用户

    @Select("SELECT * FROM user ORDER BY id DESC LIMIT #{offset}, #{size}")
    List<User> findPage(@Param("offset") int offset, @Param("size") int size); // 分页获取用户列表

    @Select("SELECT COUNT(1) FROM user")
    long count(); // 获取用户总数

    @Insert("INSERT INTO user(username,password,email,nickname,avatar,status) VALUES(#{username},#{password},#{email},#{nickname},#{avatar},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user); // 插入新用户

    @Update("UPDATE user SET email=#{email}, nickname=#{nickname}, avatar=#{avatar}, status=#{status} WHERE id=#{id}")
    int update(User user); // 更新用户信息

    @Update("UPDATE user SET password=#{password} WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password); // 更新用户密码

    @Delete("DELETE FROM user WHERE id = #{id}")
    int delete(Long id); // 删除用户
}
