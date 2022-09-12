package cn.easy.ocr.main.service.dao.mapper;

import cn.easy.ocr.main.service.dao.po.OcrRequestLog;
import cn.easy.ocr.main.service.query.RequestLogQuery;

import java.util.List;

/**
 * @author : feiya
 * @date : 2022/9/11
 * @description : 也可以 在启动类上 {@link cn.easy.ocr.main.service.Bootstrap} 使用 @MapperScan(value = {"cn.easy.ocr.main
 * .service.dao.mapper"})，
 * 就不用在此类上加 @Mapper
 */
public interface OcrRequestLogMapper {

    List<OcrRequestLog> find(RequestLogQuery requestLogQuery);

    void insert(OcrRequestLog ocrRequestLog);
}
