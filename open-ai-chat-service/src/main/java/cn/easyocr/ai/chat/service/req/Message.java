package cn.easyocr.ai.chat.service.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description :
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    /**
     * The role of the author of this message. One of system, user, or assistant
     * {@link cn.easyocr.ai.chat.service.enums.ChatRole}
     */
    @NotBlank
    private String role;

    /**
     * The contents of the message.
     */
    @NotBlank
    private String content;

    /**
     * The name of the author of this message. May contain a-z, A-Z, 0-9, and underscores, with a maximum length
     * of 64 characters.
     */
    private String name;

}
