package cn.easy.ocr.main.service.dao;

import java.util.Date;
import java.util.List;

import cn.easy.ocr.main.service.BaseTest;
import cn.easy.ocr.main.service.dao.mapper.OcrSourceInfoMapper;
import cn.easy.ocr.main.service.dao.po.OcrSourceInfo;
import cn.easy.ocr.main.service.query.SourceInfoQuery;
import cn.easyocr.common.utils.UuidUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : feiya
 * @date : 2022/9/12
 * @description :
 */
public class OcrSourceInfoDaoTest extends BaseTest {
    @Autowired
    private OcrSourceInfoMapper ocrSourceInfoMapper;

    @Test
    public void testFind() throws Exception {
        SourceInfoQuery sourceInfoQuery = new SourceInfoQuery();
        List<OcrSourceInfo> result =  ocrSourceInfoMapper.find(sourceInfoQuery);
        System.out.println(result);
    }

    @Test
    public void testInsert() throws Exception {
        OcrSourceInfo ocrSourceInfo = new OcrSourceInfo();
        ocrSourceInfo.setSourceId(UuidUtil.getUuid());
        ocrSourceInfo.setSourceName("bnaidu");
        //  ocrSourceInfo.setType();
        ocrSourceInfo.setDayLimit(0);
        ocrSourceInfo.setDayUsed(0);
        ocrSourceInfo.setMonthLimit(0);
        ocrSourceInfo.setMonthUsed(0);
        ocrSourceInfo.setParamsConfig("");
        ocrSourceInfo.setCreateTime(new Date());

        ocrSourceInfoMapper.insert(ocrSourceInfo);
    }
}
