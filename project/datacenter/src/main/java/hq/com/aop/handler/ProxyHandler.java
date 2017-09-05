package hq.com.aop.handler;

import hq.com.aop.ctx.SpringApplicationContext;
import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.utils.ClassObject;
import hq.com.aop.utils.StringUtils;
import hq.com.exception.IllegalOptionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;


/**
 * proxy handler
 * <p>
 * <p>
 * The agent method handler performs its methods according to the full class name and
 * method name through the reflection mechanism.
 * </p>
 *
 * @author yinhaiquan
 * @date 2017/05/22 11:24:26.
 */
@Component(value = "proxyHandler")
public class ProxyHandler {
    public static Logger log = LoggerFactory.getLogger("proxyHandler");

    /**
     * <p>
     * Tip:
     * the agent layer avoids using multiple overloaded methods
     * </p>
     * <p>
     * note:
     * Try not to invoke overloaded methods, When using automatic agents.
     * When using multiple overloaded methods, get the first method and execute it.
     * </p>
     *
     * @param className
     * @param methodName
     * @return
     * @throws IllegalArgumentsException
     */
    public Object handler(String className, String methodName, Map<String, String[]> params) throws IllegalArgumentsException, IllegalOptionException {
        Object objj = null;
        log.info("ProxyHandler.handler the in arguments : \n\r Bean类名[className]:{} \n\r 方法名[methodName]:{}", className, methodName);
        if (StringUtils.isEmpty(className) || StringUtils.isEmpty(methodName)) {
            throw new IllegalArgumentsException(SpringApplicationContext.getMessage("exception.params.notempty"), null, null);
        }
        try {
            Object obj = SpringApplicationContext.getBean(className);
            Class cls = obj.getClass();
            ClassObject co = new ClassObject();
            if (StringUtils.isNotEmpty(params)) {
                co = ClassMethodHandler.formatParams(params);
            }
            log.info("ProxyHandler.handler the in arguments : \n\r 参数类型:{} \n\r 参数值:{}", co.getCls(), co.getObjs());
            Method method = ClassMethodHandler.getMethod(cls, methodName, co.getCls());
            objj = method.invoke(obj, co.getObjs());
        } catch (IllegalAccessException e) {
            log.info("ProxyHandler.handler IllegalAccessException :{}", e.getMessage());
            throw new IllegalArgumentsException(StringUtils.format(SpringApplicationContext.getMessage("exception.method.authorization"), className, methodName), e.getMessage(), e);
        } catch (InvocationTargetException e) {
            log.info("ProxyHandler.handler InvocationTargetException :{}", e.getMessage());
            //此处接收被调用方法内部未被捕获的异常
            if (e.getTargetException() instanceof UndeclaredThrowableException){
                UndeclaredThrowableException ue = (UndeclaredThrowableException) e.getTargetException();
                if (ue.getUndeclaredThrowable().getCause() instanceof IllegalOptionException){
                    IllegalOptionException ie = (IllegalOptionException) ue.getUndeclaredThrowable().getCause();
                    throw ie;
                }
            }
        } catch (NoSuchMethodException e) {
            log.info("ProxyHandler.handler NoSuchMethodException :{}", e.getMessage());
            throw new IllegalArgumentsException(StringUtils.format(SpringApplicationContext.getMessage("exception.method.notfind"), className, methodName), e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.info("ProxyHandler.handler ClassNotFoundException :{}", e.getMessage());
            throw new IllegalArgumentsException(StringUtils.format(SpringApplicationContext.getMessage("exception.bean.notfind"), className), e.getMessage(), e);
        } catch(Exception e) {
            log.info("ProxyHandler.handler Exception :{}", e.getMessage());
            throw new IllegalArgumentsException(SpringApplicationContext.getMessage("exception.unknown"), e.getMessage(), e);
        }
        return objj;
    }
}
