package cn.easyocr.ai.chat.service.dao.mapper;

import cn.easyocr.ai.chat.service.dao.po.ChatMsgs;
import cn.easyocr.ai.chat.service.dao.query.ChatMsgQuery;

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

    int count(ChatMsgQuery chatMsgQuery);
    List<ChatMsgs> findBaseMsg(ChatMsgQuery chatMsgQuery);
}
