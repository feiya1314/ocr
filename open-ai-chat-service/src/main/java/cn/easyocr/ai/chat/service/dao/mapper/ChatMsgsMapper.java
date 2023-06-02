package cn.easyocr.ai.chat.service.dao.mapper;

import cn.easyocr.ai.chat.service.dao.po.ChatMsgs;
import cn.easyocr.ai.chat.service.dao.query.ChatMsgQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : feiya
 * @date : 2023/5/27
 * @description :
 */
public interface ChatMsgsMapper {

    void insert(ChatMsgs chatMsgs);

    void update(ChatMsgs chatMsgs);

    List<ChatMsgs> find(ChatMsgQuery chatMsgQuery);

    List<ChatMsgs> findChatMsgsByOrder(@Param("chatId") String chatId, @Param("lastMsgId") long lastMsgId);

    int count(ChatMsgQuery chatMsgQuery);

    List<ChatMsgs> findBaseMsg(ChatMsgQuery chatMsgQuery);
}
