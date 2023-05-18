package cn.easy.ocr.main.service.controller;

import cn.easy.ocr.main.service.ocr.OcrDelegte;
import cn.easy.ocr.main.service.request.OcrRequest;
import cn.easy.ocr.main.service.vo.OcrResultVo;
import cn.easyocr.common.enums.ResultCodeEnum;
import cn.easyocr.common.resp.BaseResponse;
import cn.easyocr.db.common.dao.annotation.ReqLogAnno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author : feiya
 * @date : 2022/6/14
 * @description :
 */
@RestController
@Slf4j
public class OcrServiceController {
    @Autowired
    private OcrDelegte ocrDelegte;

    @PostMapping("/ocr")
    @ReqLogAnno(origin = "ocr")
    public BaseResponse<OcrResultVo> ocr(@Valid @RequestBody OcrRequest request) {
        log.info("ocr request start");
        BaseResponse<OcrResultVo> response = new BaseResponse<>();
        OcrResultVo vo = ocrDelegte.invokeOcr(request);
        response.setData(vo);
        response.setCode(ResultCodeEnum.SUCCESS.getCode());
        response.setMsg("success");
        log.info("ocr request finish");
        return response;
    }
}
