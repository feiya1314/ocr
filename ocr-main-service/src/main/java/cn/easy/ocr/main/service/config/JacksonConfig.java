package cn.easy.ocr.main.service.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author : feiya
 * @date : 2022/6/17
 * @description :
 */
@Configuration
public class JacksonConfig {
//    @Bean
//    //@Primary 优先考虑，优先考虑被注解的对象注入
//    @Primary
//    @ConditionalOnMissingBean
//    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
//        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//        objectMapper.enable(JsonInclude.Include.NON_NULL, false);
//        return objectMapper;
//    }
}
