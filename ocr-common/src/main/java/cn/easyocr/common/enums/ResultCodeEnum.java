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
    AUTH_INFO_NOT_FOUND(40004, "auth_info_not_found", "auth info 不存在"),
    AUTH_IN_PROGRESS(40005, "auth_in_progress", "正在登录中"),
    AUTH_CANCELLED(40006, "auth_cancelled", "登录取消了"),
    AUTH_FAILED(40007, "auth_failed", "请先登录~"),
    OCR_SERVICE_ERROR(50000, "service_error", "服务异常了, 请稍后再试~"),
    YD_QR_ERROR(50001, "yd_qr_error", "获取二维码异常, 请稍后再试~"),
    YD_REQ_ERROR(50002, "yd_req_error", "获取二维码异常, 请稍后再试~"),
    WX_QR_ERROR(50003, "wx_qr_error", "获取二维码异常, 请稍后再试~"),
    WX_SESSION_ERROR(50004, "wx_session_error", "获取微信session错误"),
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
