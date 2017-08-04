package hq.com.aop.annotation;

import hq.com.aop.aopenum.ParamType;

import java.lang.annotation.*;

/**
 * <p>
 *
 * @Describle: 切面方法参数注解
 * </p>
 * @Author:yinhaiquan
 * @Date:2017/5/20.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Parameter {
    String value() default "";

    ParamType type() default ParamType.STRING;

    String desc() default "";
}
