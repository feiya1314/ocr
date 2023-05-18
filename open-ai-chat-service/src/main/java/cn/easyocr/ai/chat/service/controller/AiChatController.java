package cn.easyocr.ai.chat.service.controller;

import cn.easyocr.ai.chat.service.req.AiChatReq;
import cn.easyocr.ai.chat.service.resp.AiChatMsgResp;
import cn.easyocr.common.enums.ResultCodeEnum;
import cn.easyocr.common.resp.BaseResponse;
import cn.easyocr.db.common.dao.annotation.ReqLogAnno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author : feiya
 * @description :
 * @since : 2023/4/10
 */
@RestController
@Slf4j
public class AiChatController {
    @PostMapping("/chat-process")
    @ReqLogAnno(origin = "ai-chat")
    public BaseResponse<AiChatMsgResp> ocr(@Valid @RequestBody AiChatReq request) {
        log.info("ocr request start");
        BaseResponse<AiChatMsgResp> response = new BaseResponse<>();
        // OcrResultVo vo = ocrDelegte.invokeOcr(request);
        AiChatMsgResp resp = new AiChatMsgResp();
        response.setData(resp);
        response.setCode(ResultCodeEnum.SUCCESS.getCode());
        response.setMsg("success");
        log.info("ocr request finish");
        return response;
    }
}

