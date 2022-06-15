package cn.easy.ocr.main.service.controller;

import cn.easy.ocr.main.service.enums.ResultCodeEnum;
import cn.easy.ocr.main.service.request.OcrRequest;
import cn.easy.ocr.main.service.response.BaseResult;
import cn.easy.ocr.main.service.service.OcrDelegte;
import cn.easy.ocr.main.service.vo.OcrResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : feiya
 * @date : 2022/6/14
 * @description :
 */
@Controller
@Slf4j
public class OcrServiceController {
    @Autowired
    private OcrDelegte ocrDelegte;

    @ResponseBody
    @PostMapping("/ocr")
    public BaseResult<OcrResultVo> ocr(@RequestBody OcrRequest request) {
        BaseResult<OcrResultVo> response = new BaseResult<>();
        ocrDelegte.invokeOcr(request);

        response.setCode(ResultCodeEnum.SUCCESS.getCode());
        response.setMsg("success");
        return response;
    }
}
