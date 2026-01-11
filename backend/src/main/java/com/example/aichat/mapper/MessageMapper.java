package com.example.aichat.mapper;

import com.example.aichat.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Select("SELECT * FROM message WHERE conversation_id = #{conversationId} ORDER BY created_at ASC")
    List<Message> listByConversation(Long conversationId);

    @Insert("INSERT INTO message(conversation_id, role, content, tokens) VALUES(#{conversationId}, #{role}, #{content}, #{tokens})")
    int insert(Message message);
}
