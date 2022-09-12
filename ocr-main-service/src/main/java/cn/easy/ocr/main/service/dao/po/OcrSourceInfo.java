package cn.easy.ocr.main.service.dao.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author : feiya
 * @date : 2022/9/12
 * @description :
 */
@Getter
@Setter
public class OcrSourceInfo {
    private Integer id;

    private String sourceId;

    private String sourceName;

    private Integer type;

    private Integer dayLimit;

    private Integer dayUsed;

    private Integer monthLimit;

    private Integer monthUsed;

    private Integer yearLimit;

    private Integer yearUsed;

    private String paramsConfig;

    private Date createTime;

    private Date updateTime;
}
