package cn.easyocr.ai.chat.service.service;

import cn.easyocr.ai.chat.service.config.ChatConfig;
import cn.easyocr.ai.chat.service.context.ChatContext;
import cn.easyocr.ai.chat.service.context.ChatServiceResult;
import cn.easyocr.ai.chat.service.dao.mapper.ChatMsgsMapper;
import cn.easyocr.ai.chat.service.dao.po.ChatMsgs;
import cn.easyocr.ai.chat.service.dao.query.ChatMsgQuery;
import cn.easyocr.ai.chat.service.enums.ChatGptModel;
import cn.easyocr.ai.chat.service.enums.ChatRole;
import cn.easyocr.ai.chat.service.handler.StreamResult;
import cn.easyocr.ai.chat.service.req.AiChatReq;
import cn.easyocr.ai.chat.service.req.ChatOptions;
import cn.easyocr.ai.chat.service.req.Message;
import cn.easyocr.common.enums.ResultCodeEnum;
import cn.easyocr.common.exception.ParamValidateException;
import cn.easyocr.common.exception.ServiceException;
import cn.easyocr.common.utils.JsonUtils;
import cn.easyocr.common.utils.TimeUtil;
import cn.easyocr.common.utils.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Component("aiChatServiceAdapter")
public class AiChatServiceAdapter implements IAiChatService {
    @Autowired
    private ChatConfig config;
    @Autowired
    @Qualifier("api2DChatService")
    private IAiChatService api2DChatService;

    @Autowired
    private ChatMsgsMapper chatMsgsMapper;

    @Override
    public ChatServiceResult chat(ChatContext chatContext) {
        checkChatOptions(chatContext.getAiChatReq().getOptions());

        preProcessReq(chatContext);

        return api2DChatService.chat(chatContext);
    }

    private void preProcessReq(ChatContext chatContext) {
        AiChatReq aiChatReq = chatContext.getAiChatReq();
        ChatOptions options = aiChatReq.getOptions();

        // ChatOptions 中有对话信息，说明已经开启了一个会话，不是一个会话的第一个请求,需要带上之前的上下文msg信息
        if (options != null && StringUtils.hasText(options.getConversationId()) && StringUtils.hasText(options.getParentMessageId())) {
            ChatMsgQuery.ChatMsgQueryBuilder chatMsgQuery = ChatMsgQuery.builder();
            chatMsgQuery.chatId(options.getConversationId()).msgId(options.getParentMessageId());
            List<ChatMsgs> msgs = chatMsgsMapper.find(chatMsgQuery.build());

            // chat id要存在，且对应的父消息必须是ai角色的，以保证当此请求的时候，能带上之前所有的消息
            if (CollectionUtils.isEmpty(msgs) || msgs.get(0).getRole() != ChatRole.ASSISTANT.getRoleId()) {
                throw new ParamValidateException(ResultCodeEnum.PARENT_MSG_ROLE_ERROR);
            }

            chatContext.setParentMsgId(options.getParentMessageId());

            // 当次请求的上一个回答，有记录下一个消息的id，说明当此请求属于重复请求，比如重新生成请求，例如上次请求 ai返回错误,
            // 需要重新生成
            if (StringUtils.hasText(msgs.get(0).getNextMsgId())) {
                // 走重新生成的对话的逻辑
                regenerateChat(chatContext, msgs.get(0));
                return;
            }

            // 带上之前的msg 创建chat请求 更新parent msg的nextmsgId
            continueChat(chatContext, msgs.get(0));
            return;
        }

        // 新创建一个对话，因为之前没有context信息
        startNewChat(chatContext);
    }

    /**
     * 重复请求，比如重新生成请求，例如上次请求 ai返回错误, 需要重新生成。需要更新对应的ai回答的内容
     *
     * @param chatContext req
     * @param parentMsg   需要重新生成ai回答的请求对应的上个请求，重新生成请求是用户的提问，parentMsg则是上一个ai的回答
     */
    private void regenerateChat(ChatContext chatContext, ChatMsgs parentMsg) {
        ChatMsgQuery query = ChatMsgQuery.builder()
                .chatId(parentMsg.getChatId())
                .msgId(parentMsg.getNextMsgId())
                .build();

        List<ChatMsgs> reqMsgs = chatMsgsMapper.find(query);
        if (CollectionUtils.isEmpty(reqMsgs) || !chatContext.getAiChatReq().getPrompt().equals(reqMsgs.get(0).getContent())) {
            throw new ParamValidateException(ResultCodeEnum.PARAM_ERROR);
        }

        ChatMsgs reqMsg = reqMsgs.get(0);
        chatContext.setChatId(parentMsg.getChatId());
        chatContext.setReqMsgId(reqMsg.getMsgId());
        chatContext.setRespMsgId(reqMsg.getNextMsgId());
        chatContext.setParentMsgId(parentMsg.getMsgId());

        buildContextMsgs(chatContext, parentMsg);
    }

    /**
     * 继续一个会话问答，用户的请求msg 和 ai的回答需要添加新记录，同时更新 parentMsg 对应的会话的next_msg_id(当此req的msg_id),
     * 保证parentMsg 能关联到当此请求对应的msg
     *
     * @param chatContext req
     * @param parentMsg   当前请求对应的上一个请求，当前请求是用户的提问，parentMsg则是上一个ai的回答
     */
    private void continueChat(ChatContext chatContext, ChatMsgs parentMsg) {
        AiChatReq aiChatReq = chatContext.getAiChatReq();
        String chatId = parentMsg.getChatId();
        // 当前请求没有创建，需要新创建msgid
        String userReqMsgId = UuidUtil.getUuid();
        String respMsgId = UuidUtil.getUuid();

        chatContext.setChatId(chatId);
        chatContext.setReqMsgId(userReqMsgId);
        chatContext.setRespMsgId(respMsgId);

        recordReqMsg(chatContext.getUserId(), chatId, userReqMsgId, aiChatReq.getPrompt(), respMsgId);

        updateParentMsg(parentMsg.getId(), userReqMsgId);

        recordAiRespBase(chatContext.getUserId(), chatId, respMsgId);

        buildContextMsgs(chatContext, parentMsg);
    }

    /**
     * 新开启一个会话问答，因为之前没有context信息，用户的请求msg 和 ai的回答需要添加新记录
     *
     * @param chatContext req
     */
    private void startNewChat(ChatContext chatContext) {
        AiChatReq aiChatReq = chatContext.getAiChatReq();
        String chatId = UuidUtil.getUuid();
        String userReqMsgId = UuidUtil.getUuid();
        String respMsgId = UuidUtil.getUuid();

        chatContext.setChatId(chatId);
        chatContext.setReqMsgId(userReqMsgId);
        chatContext.setRespMsgId(respMsgId);
        // 记录系统角色信息
        recordSysMsg(aiChatReq,chatContext.getUserId(), chatId, userReqMsgId);

        recordReqMsg(chatContext.getUserId(), chatId, userReqMsgId, aiChatReq.getPrompt(), respMsgId);

        recordAiRespBase(chatContext.getUserId(), chatId, respMsgId);

        List<Message> messages = new ArrayList<>();
        Message sysMsg = new Message();
        sysMsg.setRole(ChatRole.SYSTEM.getRole());
        sysMsg.setContent(aiChatReq.getSystemMessage());
        messages.add(sysMsg);

        Message reqMsg = new Message();
        reqMsg.setRole(ChatRole.USER.getRole());
        reqMsg.setContent(aiChatReq.getPrompt());
        messages.add(reqMsg);

        chatContext.setReqMessages(messages);
    }

    private void buildContextMsgs(ChatContext chatContext, ChatMsgs parentMsg) {
        List<Message> contextMsgs = queryContextMessage(parentMsg);
        if (contextMsgs.size() > config.getContextMaxMsgs() + 1) {
            // 每个会话不超过 ContextMaxMsgs 个对话
            throw new ParamValidateException(ResultCodeEnum.CONTEXT_MSGS_MAX, config.getContextMaxMsgs());
        }
        Message curReqMsg = new Message();
        curReqMsg.setRole(ChatRole.USER.getRole());
        curReqMsg.setContent(chatContext.getAiChatReq().getPrompt());

        contextMsgs.add(curReqMsg);

        chatContext.setReqMessages(contextMsgs);
    }

    private List<Message> queryContextMessage(ChatMsgs parentMsg) {
        List<ChatMsgs> chatMsgs = chatMsgsMapper.findChatMsgsByOrder(parentMsg.getChatId(), parentMsg.getId());
        return chatMsgs.stream().map(msg -> {
            Message message = new Message();
            message.setRole(ChatRole.getRoleById(msg.getRole()));
            if (msg.getRole() == ChatRole.ASSISTANT.getRoleId()) {
                StreamResult streamResult = JsonUtils.jsonToBean(msg.getContent(), StreamResult.class);
                if (streamResult == null) {
                    throw new ServiceException(ResultCodeEnum.OCR_SERVICE_ERROR);
                }
                message.setContent(streamResult.getContent());
                return message;
            }
            message.setContent(msg.getContent());
            return message;
        }).collect(Collectors.toList());
    }

    private void updateParentMsg(long id, String nextMsgId) {
        ChatMsgs msg = new ChatMsgs();
        msg.setId(id);
        msg.setNextMsgId(nextMsgId);
        msg.setTimestamp(System.currentTimeMillis());
        chatMsgsMapper.update(msg);
    }

    private void recordReqMsg(Long userId, String chatId, String msgId, String content, String respMsgId) {
        ChatMsgs msg = new ChatMsgs();
        msg.setUserId(userId);
        msg.setModel(ChatGptModel.GPT_3_5_TURBO.getModelId());
        msg.setChatId(chatId);
        msg.setMsgId(msgId);
        msg.setNextMsgId(respMsgId);
        msg.setContent(content);
        msg.setRole(ChatRole.USER.getRoleId());
        msg.setTimestamp(System.currentTimeMillis());
        msg.setPtd(TimeUtil.getPtd());

        // 记录当前的请求
        chatMsgsMapper.insert(msg);
    }

    private void recordAiRespBase(Long userId, String chatId, String respMsgId) {
        ChatMsgs msg = new ChatMsgs();
        msg.setUserId(userId);
        msg.setChatId(chatId);
        msg.setMsgId(respMsgId);
        msg.setRole(ChatRole.ASSISTANT.getRoleId());
        msg.setTimestamp(System.currentTimeMillis());
        msg.setPtd(TimeUtil.getPtd());

        chatMsgsMapper.insert(msg);
    }

    private void recordSysMsg(AiChatReq aiChatReq, Long userId, String chatId, String nexMsgId) {
        ChatMsgs msg = new ChatMsgs();
        msg.setUserId(userId);
        msg.setModel(ChatGptModel.GPT_3_5_TURBO.getModelId());
        msg.setChatId(chatId);
        msg.setMsgId(UuidUtil.getUuid());
        msg.setNextMsgId(nexMsgId);
        msg.setContent(aiChatReq.getSystemMessage());
        msg.setRole(ChatRole.SYSTEM.getRoleId());
        msg.setTimestamp(System.currentTimeMillis());
        msg.setPtd(TimeUtil.getPtd());

        chatMsgsMapper.insert(msg);
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
        throw new ParamValidateException(ResultCodeEnum.CHAT_ID_PARENT_ID_BOTH);
    }
}
