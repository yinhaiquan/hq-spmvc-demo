package hq.com.aop.annotation;

import hq.com.aop.aopenum.Tag;

import java.lang.annotation.*;

/**
 * Created by yinhaiquan on 2017/5/20.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    Tag isCache() default Tag.YES;

    String key() default "";
}
