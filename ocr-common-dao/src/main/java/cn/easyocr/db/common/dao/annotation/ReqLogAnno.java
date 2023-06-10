package cn.easyocr.db.common.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : feiya
 * @date : 2022/10/5
 * @description :
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReqLogAnno {
    String origin();

    boolean asyncReq() default false;
}
