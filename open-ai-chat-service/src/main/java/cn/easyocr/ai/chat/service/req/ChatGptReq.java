package cn.easyocr.ai.chat.service.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * @author : feiya
 * @date : 2023/5/20
 * @description : <a href="https://platform.openai.com/docs/api-reference/chat/create">...</a>
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatGptReq {
    /**
     * {@link cn.easyocr.ai.chat.service.enums.ChatGptModel}
     */
    @NotBlank
    private String model;

    @NotEmpty
    private List<Message> messages;

    /**
     * What sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the output more random,
     * while lower values like 0.2 will make it more focused and deterministic.
     * <p>
     * Defaults to 1
     * <p>
     * We generally recommend altering this or top_p but not both.
     * <p>
     * 用于控制生成文本的创造性和随机性程度,值越大，生成的结果越随机，更低，则结果更确定，意思是同一个问题，每次得到的答案和上次是否一致
     * 同一个问题，每次得到的答案不一定完全一致
     */
    private Double temperature;

    /**
     * An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of
     * the tokens with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass
     * are considered.
     * Defaults to 1
     * <p>
     * We generally recommend altering this or temperature but not both.
     * 参数用于控制生成回答时的多样性。它与模型的输出概率分布相关，指定了累积概率的上限。模型会根据概率分布对候选词进行排序，并从累积概率低于或等于"top_p"的候选词中进行抽样。
     * 较小的"top_p"值会限制模型从概率最高的候选词中进行选择，从而生成较为保守和确定性的回答。而较大的"top_p"值允许更多候选词参与选择，增加回答的多样性。
     * 可以通过调整"top_p"参数来控制ChatGPT生成回答的多样性程度。较小的值会使回答更加集中和可控，而较大的值会产生更多不同的可能性和多样性。根据具体应用需求，您可以自行设置合适的"top_p"值。
     */
    private Double top_p;

    /**
     * How many chat completion choices to generate for each input message.
     * Defaults to 1
     * 用于指定要从模型生成的候选回答中返回的最大数量。
     * 通过设置"n"参数，您可以控制API返回的候选回答的数量。例如，如果将"n"设置为3，则API将返回最多3个候选回答供您选择或展示给用户。
     * <p>
     * 这对于需要多个备选回答的应用场景很有用，例如构建聊天机器人或多轮对话系统。您可以根据需要调整"n"参数，以获得适当数量的候选回答供选择或展示。请注意，返回的候选回答按照它们在模型生成过程中的出现顺序排列。
     */
    private Integer n;

    /**
     * Defaults to false
     * <p>
     * If set, partial message deltas will be sent, like in ChatGPT. Tokens will be sent as data-only server-sent
     * events as they become available,
     * with the stream terminated by a data: [DONE] message. See the OpenAI Cookbook for example code.
     * <p>
     * 是否使用流式传输
     */
    private Boolean stream;

    /**
     * Up to 4 sequences where the API will stop generating further tokens.
     * <p>
     * "stop"参数用于指定一个停止标记（或称为停止序列），以告知模型在生成回答时停止生成文本。
     * <p>
     * 通常情况下，模型会持续生成文本，直到达到最大长度限制或生成一个完整的回答。但是，通过使用"stop"参数，您可以在特定的标记出现时指示模型停止生成文本。
     * <p>
     * 例如，如果您将"stop"参数设置为字符串列表 ["\n", "##END##"]，则当模型生成的文本中出现换行符或"##END##"时，模型会停止继续生成文本。
     * <p>
     * 使用"stop"参数可以帮助您控制生成文本的长度和确保生成的回答在特定位置停止，而不是继续无限制地生成。
     * <p>
     * 请注意，"stop"参数的效果依赖于模型的训练数据和模型的理解能力。确保选择的停止标记在模型的上下文中有明确的含义和可识别性。
     */
    private List<String> stop;

    /**
     * The maximum number of tokens to generate in the chat completion.
     * <p>
     * The total length of input tokens and generated tokens is limited by the model's context length.
     */
    private Integer max_tokens;

    /**
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so
     * far, increasing the model's likelihood to talk about new topics.
     */
    private Double presence_penalty;

    /**
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text
     * so far,
     * decreasing the model's likelihood to repeat the same line verbatim.
     */
    private Double frequency_penalty;

    /**
     * Modify the likelihood of specified tokens appearing in the completion.
     * <p>
     * Accepts a json object that maps tokens (specified by their token ID in the tokenizer) to an associated bias
     * value from -100 to 100. Mathematically, the bias is added to the logits generated by the model prior to
     * sampling. The exact effect will vary per model, but values between -1 and 1 should decrease or increase
     * likelihood of selection; values like -100 or 100 should result in a ban or exclusive selection of the relevant
     * token.
     */
    private Map logit_bias;

    /**
     * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse
     */
    private String user;

}
