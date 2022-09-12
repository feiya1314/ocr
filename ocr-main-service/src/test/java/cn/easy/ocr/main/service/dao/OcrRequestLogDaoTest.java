package cn.easy.ocr.main.service.dao;

import cn.easy.ocr.main.service.BaseTest;
import cn.easy.ocr.main.service.dao.mapper.OcrRequestLogMapper;
import cn.easy.ocr.main.service.dao.po.OcrRequestLog;
import cn.easy.ocr.main.service.query.RequestLogQuery;
import cn.easy.ocr.main.service.utils.UuidUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author : feiya
 * @date : 2022/9/12
 * @description :
 */
public class OcrRequestLogDaoTest extends BaseTest {
    @Autowired
    private OcrRequestLogMapper ocrRequestLogMapper;

    @Test
    public void testInsert() throws Exception {
        OcrRequestLog log = new OcrRequestLog();
        log.setIp("10.32.43.55");
        log.setUserId("34233242523454");
        log.setRequestId(UuidUtil.getUuid());
       // log.setUa("uatest");
        //log.setRefer("refertest");
        log.setTimestamp(System.currentTimeMillis());
        log.setSdevId("devidsds");
        log.setDeviceData("devdata");
        log.setPtd(20220912);

        ocrRequestLogMapper.insert(log);
    }

    @Test
    public void testFind() throws Exception {
        RequestLogQuery sourceInfoQuery = new RequestLogQuery();
        List<OcrRequestLog> result = ocrRequestLogMapper.find(sourceInfoQuery);
        System.out.println(result);
    }
}
