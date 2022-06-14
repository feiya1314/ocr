package cn.easy.ocr.main.service.controller;

import cn.easy.ocr.main.service.request.OcrRequest;
import cn.easy.ocr.main.service.response.BaseResult;
import cn.easy.ocr.main.service.vo.OcrResultVo;
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
public class OcrServiceController {
    @ResponseBody
    @PostMapping("/ocr")
    public BaseResult<OcrResultVo> ocr(@RequestBody OcrRequest request) {
        BaseResult<OcrResultVo> response = new BaseResult<>();
        response.setCode(0);
        response.setMsg("success");
        return response;
    }
}
