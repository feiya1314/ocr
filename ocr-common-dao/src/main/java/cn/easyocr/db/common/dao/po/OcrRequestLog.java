package cn.easyocr.db.common.dao.po;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : feiya
 * @date : 2022/9/12
 * @description :
 */
@Getter
@Setter
public class OcrRequestLog {
    private Long id;

    private String ip;

    private String userId;

    private String requestId;
    
    private String origin;

    private String ua;

    private String refer;

    private Long timestamp;

    private String sdevId;

    private String deviceData;

    private Integer ptd;
}
