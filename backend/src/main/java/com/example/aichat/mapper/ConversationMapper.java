package com.example.aichat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.aichat.entity.Conversation;

@Mapper // 标注为 MyBatis 映射接口
public interface ConversationMapper {

    @Select("SELECT * FROM conversation WHERE user_id = #{userId} ORDER BY updated_at DESC")
    List<Conversation> listByUser(Long userId); // 根据用户 ID 获取会话列表

    @Select("SELECT * FROM conversation WHERE id = #{id} AND user_id = #{userId}")
    Conversation findByIdAndUser(@Param("id") Long id, @Param("userId") Long userId); // 根据会话 ID 和用户 ID 查找会话

    @Insert("INSERT INTO conversation(user_id,title) VALUES(#{userId},#{title})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Conversation conversation); // 插入新会话

    @Update("UPDATE conversation SET title=#{title} WHERE id=#{id} AND user_id=#{userId}")
    int update(Conversation conversation); // 更新会话标题

    @Update("UPDATE conversation SET updated_at = NOW() WHERE id=#{id}")
    int touch(Long id); // 更新会话的更新时间

    @Delete("DELETE FROM conversation WHERE id = #{id} AND user_id = #{userId}")
    int delete(@Param("id") Long id, @Param("userId") Long userId); // 删除会话
}
