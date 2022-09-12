package cn.easy.ocr.main.service.dao.mapper;

import cn.easy.ocr.main.service.dao.po.OcrRequestLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * @author : feiya
 * @date : 2022/9/11
 * @description : 也可以 在启动类上 {@link cn.easy.ocr.main.service.Bootstrap} 使用 @MapperScan(value = {"cn.easy.ocr.main.service.dao.mapper"})，
 * 就不用在此类上加 @Mapper
 */
@Mapper
public interface OcrRequestLogMapper {
    @Insert("INSERT INTO ocr_request_log(ip, user_id, request_id, ua,refer,timestamp,sdev_id,device_data,ptd) " +
            "VALUES(#{ip}, #{userId}, #{requestId}, #{ua}, #{refer}, #{timestamp}, #{sdevId}, #{deviceData}, #{ptd})")
//    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(OcrRequestLog ocrRequestLog);
}
