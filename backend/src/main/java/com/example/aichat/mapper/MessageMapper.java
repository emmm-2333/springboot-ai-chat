package com.example.aichat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.aichat.entity.Message;

@Mapper // 标注为 MyBatis 映射接口
public interface MessageMapper {

    @Select("SELECT * FROM message WHERE conversation_id = #{conversationId} ORDER BY created_at ASC")
    List<Message> listByConversation(Long conversationId); // 根据会话 ID 获取消息列表

    @Insert("INSERT INTO message(conversation_id, role, content, tokens) VALUES(#{conversationId}, #{role}, #{content}, #{tokens})")
    int insert(Message message); // 插入新消息
}
