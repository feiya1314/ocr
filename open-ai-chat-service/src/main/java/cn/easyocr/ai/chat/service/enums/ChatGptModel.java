package cn.easyocr.ai.chat.service.enums;

/**
 * @author : feiya
 * @date : 2023/5/20
 * @description : 请求 chatgpt 模型<a href="https://platform.openai.com/docs/models/model-endpoint-compatibility">...</a>
 */
public enum ChatGptModel {
    // gpt chat模型
    GPT_4("user", "gpt-4"),
    GPT_3_5_TURBO("gpt-3.5-turbo", "gpt-3.5-turbo 模型"),
    ;

    private final String model;
    private final String desc;

    ChatGptModel(String model, String desc) {
        this.model = model;
        this.desc = desc;
    }

    public String getModel() {
        return model;
    }

    public String getDesc() {
        return desc;
    }
}
