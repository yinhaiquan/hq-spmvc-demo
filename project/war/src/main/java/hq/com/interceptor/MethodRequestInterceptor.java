package hq.com.interceptor;

import hq.com.aop.interceptor.AbstractMethodRequestInterceptor;
import hq.com.aop.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Describle: AOP切面缓存、日志处理
 * @Author: YinHq
 * @Date: Created By 下午 3:37 2017/5/21 0021
 * @Modified By
 */
public class MethodRequestInterceptor extends AbstractMethodRequestInterceptor {
    public Object getCache(String key) {
        return null;
    }

    public void setCache(String key, Object result) {

    }

    public void preLog(Class name, String methodName, Object[] params, String title) {
        Logger log = LoggerFactory.getLogger(name);
        log.info("\n\r 类名-方法名:{}-{}\n\r 入参：{}", name.getName(), methodName, StringUtils.format(title, params));
    }

    public void afterLog(Class name, String methodName, Object obj, String title) {
        Logger log = LoggerFactory.getLogger(name);
        log.info("\n\r 类名-方法名:{}-{}\n\r 返回结果：{}", name.getName(), methodName, obj);
    }
}
