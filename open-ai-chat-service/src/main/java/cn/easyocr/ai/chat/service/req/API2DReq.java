package cn.easyocr.ai.chat.service.req;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2023/5/21
 * @description : <a href="https://api2d.com/wiki/doc#api-list">...</a>
 */
@Getter
@Setter
public class API2DReq extends ChatGptReq {
    /**
     * 默认为 false，为 true 时会尝试让 GPT 自己审查内容，不输出违规结果。由于 GPT 的调性，效果好坏比较随机。总的来说对暴力、色情内容效果较好，政治类效果一般。
     * 开启后会每次访问会增加约 1P 的消耗
     */
    private Boolean safe_mode;
    /**
     * 默认为 false，为 true 时会调用文本安全接口对内容进行判定，并将审核结果添加到返回值中的 moderation 字段，开发者可以根据值自行判断如何处理。
     * 审核输出的详细解释见这里。开启后每 9000 字符会增加 10P 的消耗
     */
    public Boolean moderation;

    /**
     * 默认为 false，在 moderation 为 true 且自身也为 true 时，如果审核结果不是 Pass，将自动进行内容拦截，对流也生效
     */
    public Boolean moderation_stop;
}
