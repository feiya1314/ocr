package cn.easyocr.db.common.dao.mapper;

import cn.easyocr.db.common.dao.po.OcrRequestLog;
import cn.easyocr.db.common.dao.query.RequestLogQuery;

import java.util.List;

/**
 * @author : feiya
 * @date : 2022/9/11
 * @description : 也可以 在启动类上 { cn.easy.ocr.main.service.Bootstrap}
 * 使用 @MapperScan(value = {"cn.easy.ocr.main.service.dao.mapper"})，
 * 就不用在此类上加 @Mapper
 */
public interface OcrRequestLogMapper {

    List<OcrRequestLog> find(RequestLogQuery requestLogQuery);

    void insert(OcrRequestLog ocrRequestLog);
}
