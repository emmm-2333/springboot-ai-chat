package com.example.aichat.mapper;

import com.example.aichat.entity.Conversation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConversationMapper {

    @Select("SELECT * FROM conversation WHERE user_id = #{userId} ORDER BY updated_at DESC")
    List<Conversation> listByUser(Long userId);

    @Select("SELECT * FROM conversation WHERE id = #{id} AND user_id = #{userId}")
    Conversation findByIdAndUser(@Param("id") Long id, @Param("userId") Long userId);

    @Insert("INSERT INTO conversation(user_id,title) VALUES(#{userId},#{title})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Conversation conversation);

    @Update("UPDATE conversation SET title=#{title} WHERE id=#{id} AND user_id=#{userId}")
    int update(Conversation conversation);

    @Update("UPDATE conversation SET updated_at = NOW() WHERE id=#{id}")
    int touch(Long id);

    @Delete("DELETE FROM conversation WHERE id = #{id} AND user_id = #{userId}")
    int delete(@Param("id") Long id, @Param("userId") Long userId);
}
