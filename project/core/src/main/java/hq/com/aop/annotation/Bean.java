package hq.com.aop.annotation;

import hq.com.aop.aopenum.Tag;

import java.lang.annotation.*;

/**
 * <p>
 *
 * @Describle: 切面类/方法注解
 * </p>
 * @Author:yinhaiquan
 * @Date:2017/5/20.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
}
