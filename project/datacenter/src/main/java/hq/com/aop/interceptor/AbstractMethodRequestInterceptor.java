package hq.com.aop.interceptor;

import hq.com.aop.annotation.Cache;
import hq.com.aop.annotation.Log;
import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.utils.StringUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * <p>
 *
 * @Describle: 方法请求AOP拦截器
 * </p>
 * @Author:yinhaiquan
 * @Date:2017/5/20.
 */
public abstract class AbstractMethodRequestInterceptor implements MethodInterceptor {

    @Override
    public final Object invoke(MethodInvocation methodInvocation) throws IllegalArgumentsException {
        Object result = null;
        try {
            result = null;
            /**
             * 此处自定义注解只对引用方法上的注解才有效，否则无效。
             * 比如service serviceimpl引用service则注解必须加载service的方法上才生效
             */
            Cache cache = AnnotationUtils.findAnnotation(methodInvocation.getMethod(), Cache.class);
            Log log = AnnotationUtils.findAnnotation(methodInvocation.getMethod(), Log.class);

            if (StringUtils.isNotEmpty(cache)) {
                //Cache缓存注解处理
                if (cache.isCache().isTag()) {
                    Object cacheResult = null;
                    cacheResult = getCache(cache.key());
                    if (null == cacheResult) {
                        result = methodInvocation.proceed();
                        setCache(cache.key(), result);
                        return result;
                    } else {
                        return cacheResult;
                    }
                }
            }
            //执行方法
            result = methodInvocation.proceed();
            if (StringUtils.isNotEmpty(log)) {
                //Log注解日志处理
                Class name = methodInvocation.getThis().getClass();
                String methodName = methodInvocation.getMethod().getName();
                Object[] params = methodInvocation.getArguments();
                String title = log.desc();
                preLog(name, methodName, params, title);
                afterLog(name, methodName, result, title);
            }
        } catch (Throwable throwable) {
            throw new IllegalArgumentsException("aop class/method exception", throwable.getMessage(), throwable);
        }
        return result;
    }

    public abstract Object getCache(String key);

    public abstract void setCache(String key, Object result);

    public abstract void preLog(Class name, String methodName, Object[] params, String title);

    public abstract void afterLog(Class name, String methodName, Object obj, String title);

}
