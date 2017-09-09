package hq.com.aop.annotation;

import java.lang.annotation.*;

/**
 * @title : 表字段属性注解
 * @describle :
 * <p>
 *     <b>note:</b>
 *     该注解作用于获取数据库对应表字段
 * </p>
 * Create By yinhaiquan
 * @date 2017/9/7 17:16 星期四
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableField {
    String value() default "";
}
