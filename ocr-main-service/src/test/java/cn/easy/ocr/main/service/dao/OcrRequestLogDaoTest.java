package cn.easy.ocr.main.service.dao;

import cn.easy.ocr.main.service.BaseTest;
import cn.easy.ocr.main.service.dao.mapper.OcrRequestLogMapper;
import cn.easy.ocr.main.service.dao.po.OcrRequestLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        log.setId(11L);
        log.setIp("10.32.43.55");
        log.setUserId("34233242523454");
        log.setRequestId("sdjsds7yrteuyi887");
        log.setUa("uatest");
        log.setRefer("refertest");
        log.setTimestamp(System.currentTimeMillis());
        log.setSdevId("devidsds");
        log.setDeviceData("devdata");
        log.setPtd(20220912);

        ocrRequestLogMapper.insert(log);
    }
}
