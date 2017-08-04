package hq.com.aop.annotation;

import hq.com.aop.aopenum.Tag;

import java.lang.annotation.*;

/**
 * @Describle: 日志标记
 * @Author: YinHq
 * @Date: Created By 下午 5:13 2017/5/21 0021
 * @Modified By
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    Tag value() default Tag.YES;

    String desc() default "";
}
