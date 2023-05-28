package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.context.ChatServiceResult;
import cn.easyocr.ai.chat.service.dao.mapper.ChatMsgsMapper;
import cn.easyocr.ai.chat.service.dao.po.ChatMsgs;
import cn.easyocr.ai.chat.service.dao.query.ChatMsgQuery;
import cn.easyocr.ai.chat.service.enums.ChatRole;
import cn.easyocr.ai.chat.service.req.AiChatReq;
import cn.easyocr.ai.chat.service.req.ChatOptions;
import cn.easyocr.common.enums.ResultCodeEnum;
import cn.easyocr.common.exception.ParamValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Component("aiChatServiceAdapter")
public class AiChatServiceAdapter implements IAiChatService {
    @Autowired
    @Qualifier("api2DChatService")
    private IAiChatService api2DChatService;

    @Autowired
    private ChatMsgsMapper chatMsgsMapper;

    @Override
    public ChatServiceResult chat(ChatContext chatContext) {
        AiChatReq aiChatReq = chatContext.getAiChatReq();
        preProcessReq(aiChatReq);

        return api2DChatService.chat(chatContext);
    }

    private void preProcessReq(AiChatReq aiChatReq) {
        ChatOptions options = aiChatReq.getOptions();
        checkChatOptions(options);

        ChatMsgs chatMsgs = new ChatMsgs();
        // ChatOptions 中有对话信息，说明已经开启了一个会话，不是一个会话的第一个请求,需要带上之前的上下文msg信息
        if (options != null && StringUtils.hasText(options.getConversationId()) && StringUtils.hasText(options.getParentMessageId())) {
            ChatMsgQuery.ChatMsgQueryBuilder chatMsgQuery = ChatMsgQuery.builder();
            chatMsgQuery.chatId(options.getConversationId()).msgId(options.getParentMessageId());
            List<ChatMsgs> msgs = chatMsgsMapper.find(chatMsgQuery.build());

            // chat id要存在，且对应的父消息必须是ai角色的，以保证当此请求的时候，能带上之前所有的消息
            if (CollectionUtils.isEmpty(msgs) || msgs.get(0).getRole() != ChatRole.ASSISTANT.getRoleId()) {
                throw new ParamValidateException(ResultCodeEnum.PARAM_ERROR);
            }

            // 当次请求的上一个回答，有记录下一个消息的id，说明当此请求属于重复请求，比如重新生成请求，例如上次请求 ai返回错误,
            // 需要重新生成
            if (StringUtils.hasText(msgs.get(0).getNextMsgId())) {
                // todo 走重新生成的对话的逻辑
                return;
            }

            // todo 带上之前的msg 创建chat请求
            return;
        }

        // todo 新创建一个对话，没有之前的context信息
        //chatMsgs.setUserId();
//        chatMsgs.setModel();
//        chatMsgs.setChatId();
//        chatMsgs.setMsgId();
//        chatMsgs.setNextMsgId();
//        chatMsgs.setContent();
//        chatMsgs.setRole();
//        chatMsgs.setTimestamp(System.currentTimeMillis());
//        chatMsgs.setPtd(TimeUtil.getPtd());
//
//        chatMsgsMapper.insert();
    }

    private void checkChatOptions(ChatOptions options) {
        if (options == null) {
            return;
        }

        if (!StringUtils.hasText(options.getConversationId()) && !StringUtils.hasText(options.getParentMessageId())) {
            return;
        }

        if (StringUtils.hasText(options.getConversationId()) && StringUtils.hasText(options.getParentMessageId())) {
            return;
        }

        // chat id 和 parent msg 要全部存在
        throw new ParamValidateException(ResultCodeEnum.PARAM_ERROR);
    }
}
