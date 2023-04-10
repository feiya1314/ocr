package cn.easy.ocr.main.service;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用启动服务
 *
 * @author : feiya
 * @date : 2022/6/14
 * @description : 应用启动服务入口
 */
@SpringBootApplication
@MapperScan(value = {"cn.easy.ocr.main.service.dao.mapper","cn.easyocr.db.common.dao.mapper"})
@Slf4j
public class Bootstrap {
    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
        log.info("start ocr portal service success");
    }
}
