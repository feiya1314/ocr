package cn.easyocr.ai.chat.service.enums;

/**
 * @author : feiya
 * @date : 2023/5/20
 * @description : 请求 chatgpt 模型<a href="https://platform.openai.com/docs/models/model-endpoint-compatibility">...</a>
 */
public enum ChatGptModel {
    // gpt chat模型
    GPT_4("gpt-4", 1, "gpt-4"),
    GPT_3_5_TURBO("gpt-3.5-turbo", 2, "gpt-3.5-turbo 模型"),
    ;

    private final String model;
    private final int modelId;
    private final String desc;

    ChatGptModel(String model, int modelId, String desc) {
        this.model = model;
        this.modelId = modelId;
        this.desc = desc;
    }

    public int getModelId() {
        return modelId;
    }

    public String getModel() {
        return model;
    }

    public String getDesc() {
        return desc;
    }
}
