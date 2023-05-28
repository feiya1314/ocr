package cn.easyocr.ai.chat.service.dao;

import cn.easyocr.ai.chat.service.BaseTest;
import cn.easyocr.ai.chat.service.dao.mapper.ChatMsgsMapper;
import cn.easyocr.ai.chat.service.dao.po.ChatMsgs;
import cn.easyocr.ai.chat.service.dao.query.ChatMsgQuery;
import cn.easyocr.ai.chat.service.enums.ChatGptModel;
import cn.easyocr.ai.chat.service.enums.ChatRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author : feiya
 * @date : 2023/5/28
 * @description :
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChatMsgsMapperTest extends BaseTest {
    private static long curId = 0;
    private static long curTime = System.currentTimeMillis();
    @Autowired
    private ChatMsgsMapper chatMsgsMapper;

    @Test
    @Order(1)
    public void testInset() throws Exception {
        ChatMsgs chatMsgs = new ChatMsgs();

        chatMsgs.setUserId("user" + curTime);
        chatMsgs.setModel(ChatGptModel.GPT_3_5_TURBO.getModelId());
        chatMsgs.setChatId("chatid" + curTime);
        chatMsgs.setMsgId("msgId" + curTime);
        chatMsgs.setNextMsgId("setMsgId" + curTime);
        chatMsgs.setContent("setMsgId1" + curTime);
        chatMsgs.setRole(ChatRole.ASSISTANT.getRoleId());
        chatMsgs.setTimestamp(System.currentTimeMillis());
        chatMsgs.setPtd(20231123);

        chatMsgsMapper.insert(chatMsgs);
    }

    @Test
    @Order(2)
    public void testFindBase() throws Exception {
        ChatMsgQuery.ChatMsgQueryBuilder chatMsgQuery = ChatMsgQuery.builder();
        chatMsgQuery.chatId("chatid" + curTime);
        chatMsgQuery.msgId("msgId" + curTime);

        List<ChatMsgs> msgs = chatMsgsMapper.findBaseMsg(chatMsgQuery.build());
        Assertions.assertNotNull(msgs);
        Assertions.assertEquals(1, msgs.size());

        curId = msgs.get(0).getId();
    }

    @Test
    @Order(3)
    public void testFind() throws Exception {
        ChatMsgQuery.ChatMsgQueryBuilder chatMsgQuery = ChatMsgQuery.builder();
        chatMsgQuery.chatId("chatid" + curTime);
        chatMsgQuery.msgId("msgId" + curTime);

        List<ChatMsgs> msgs = chatMsgsMapper.find(chatMsgQuery.build());
        Assertions.assertNotNull(msgs);
        Assertions.assertEquals(1, msgs.size());
        Assertions.assertEquals((long) msgs.get(0).getId(), curId);
    }

    @Test
    @Order(4)
    public void testUpdate() throws Exception {
        ChatMsgs chatMsgs = new ChatMsgs();
        chatMsgs.setId(curId);
        chatMsgs.setNextMsgId("updatepid" + curTime);
        chatMsgs.setContent("updatecontent" + curTime);
        chatMsgs.setTimestamp(System.currentTimeMillis());

        chatMsgsMapper.update(chatMsgs);
    }
}
