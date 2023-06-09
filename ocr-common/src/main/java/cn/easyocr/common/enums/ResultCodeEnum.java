package cn.easyocr.common.enums;

import java.text.MessageFormat;

/**
 * @author : feiya
 * @date : 2022/6/15
 * @description :
 */
public enum ResultCodeEnum {
    SUCCESS(0, "success", "成功"),
    PARAM_ERROR(40000, "param_error", "请求参数错误"),
    CONTEXT_MSGS_MAX(40001, "context_msgs_max", "当前支持最大对话为 {0}，请新建一个聊天吧"),
    PARENT_MSG_ROLE_ERROR(40002, "parent_msg_role_error", "ParentMessageId 角色错误"),
    CHAT_ID_PARENT_ID_BOTH(40003, "chat_id_parent_id_both", "ParentMessageId 和chatId 需要全部存在"),
    OCR_SERVICE_ERROR(50000, "service_error", "ocr服务异常"),
    ;
    private final int code;

    private final String key;
    private final String desc;

    ResultCodeEnum(int code, String key, String desc) {
        this.code = code;
        this.desc = desc;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String formatMsg(ResultCodeEnum resultCode, Object... msgs) {
        return MessageFormat.format(resultCode.getDesc(), msgs);
    }
}
