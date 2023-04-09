package cn.easyocr.ai.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : feiya
 * @description : boot the service
 * @since : 2023/4/9
 */
@SpringBootApplication
@MapperScan(value = {"cn.easy.ai.chat.service"})
@Slf4j
public class Bootstrap {
    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
        log.info("start chat service success");
    }
}