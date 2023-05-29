package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.context.ChatServiceResult;
import cn.easyocr.ai.chat.service.dao.mapper.ChatMsgsMapper;
import cn.easyocr.ai.chat.service.dao.po.ChatMsgs;
import cn.easyocr.ai.chat.service.dao.query.ChatMsgQuery;
import cn.easyocr.ai.chat.service.enums.ChatGptModel;
import cn.easyocr.ai.chat.service.enums.ChatRole;
import cn.easyocr.ai.chat.service.req.AiChatReq;
import cn.easyocr.ai.chat.service.req.ChatOptions;
import cn.easyocr.common.enums.ResultCodeEnum;
import cn.easyocr.common.exception.ParamValidateException;
import cn.easyocr.common.utils.TimeUtil;
import cn.easyocr.common.utils.UuidUtil;
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
                // 走重新生成的对话的逻辑
                regenerateChat(aiChatReq, msgs.get(0));
                return;
            }

            // 带上之前的msg 创建chat请求 更新parent msg的nextmsgId
            continueChat(aiChatReq, msgs.get(0));
            return;
        }

        // 新创建一个对话，因为之前没有context信息
        startNewChat(aiChatReq);
    }

    /**
     * 重复请求，比如重新生成请求，例如上次请求 ai返回错误, 需要重新生成。需要更新对应的ai回答的内容
     *
     * @param aiChatReq req
     * @param parentMsg 需要重新生成ai回答的请求对应的上个请求，重新生成请求是用户的提问，parentMsg则是上一个ai的回答
     */
    private void regenerateChat(AiChatReq aiChatReq, ChatMsgs parentMsg) {

    }

    /**
     * 继续一个会话问答，用户的请求msg 和 ai的回答需要添加新记录，同时更新 parentMsg 对应的会话的next_msg_id(当此req的msg_id),
     * 保证parentMsg 能关联到当此请求对应的msg
     *
     * @param aiChatReq req
     * @param parentMsg 当前请求对应的上一个请求，当前请求是用户的提问，parentMsg则是上一个ai的回答
     */
    private void continueChat(AiChatReq aiChatReq, ChatMsgs parentMsg) {

    }

    /**
     * 新开启一个会话问答，因为之前没有context信息，用户的请求msg 和 ai的回答需要添加新记录
     *
     * @param aiChatReq req
     */
    private void startNewChat(AiChatReq aiChatReq) {
        ChatMsgs.ChatMsgsBuilder chatMsgs = ChatMsgs.builder();
        String systemMsgId = UuidUtil.getUuid();
        String chatId = UuidUtil.getUuid();
        //  chatMsgs.setUserId();
        chatMsgs.model(ChatGptModel.GPT_3_5_TURBO.getModelId())
                .chatId(chatId)
                .msgId(systemMsgId)
                .content(aiChatReq.getSystemMessage())
                .role(ChatRole.SYSTEM.getRoleId())
                .timestamp(System.currentTimeMillis())
                .ptd(TimeUtil.getPtd());

        ChatMsgs systemMsg = chatMsgs.build();
        chatMsgsMapper.insert(systemMsg);

        systemMsg.setContent(aiChatReq.getPrompt());
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
