package cn.easyocr.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : feiya
 * @date : 2022/6/17
 * @description :
 */
@Slf4j
public class JsonUtils {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 对象转为字符串
     *
     * @param value 对象
     * @param <T>   泛型
     * @return json字符串
     */
    public static <T> String toJson(T value) {
        if (value == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("error to convert to json", e);
        }
        return null;
    }

    /**
     * json字符串转为对应实例
     *
     * @param json  json字符串
     * @param clazz 类型
     * @param <T>   泛型
     * @return json转成T类型的实例
     */
    public static <T> T jsonToBean(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("error to read  value from json", e);
        }
        return null;
    }

    /**
     * json字符串转为对应实例
     *
     * @param json  json
     * @param clazz 类型
     * @param <T>   泛型
     * @return json转成T类型的实例
     */
    public static <T> T jsonToBean(String json, TypeReference<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("error to read  value from json", e);
        }
        return null;
    }
}
