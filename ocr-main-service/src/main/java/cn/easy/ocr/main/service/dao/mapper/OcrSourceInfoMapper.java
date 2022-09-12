package cn.easy.ocr.main.service.dao.mapper;

import cn.easy.ocr.main.service.dao.po.OcrSourceInfo;
import cn.easy.ocr.main.service.query.SourceInfoQuery;

import java.util.List;

/**
 * @author : feiya
 * @date : 2022/9/11
 * @description : 也可以 在启动类上 {@link cn.easy.ocr.main.service.Bootstrap} 使用 @MapperScan(value = {"cn.easy.ocr.main
 * .service.dao.mapper"})，
 * 也可以此类上加 @Mapper
 */
public interface OcrSourceInfoMapper {
    List<OcrSourceInfo> find(SourceInfoQuery sourceInfoQuery);

    void insert(OcrSourceInfo ocrSourceInfo);
}
