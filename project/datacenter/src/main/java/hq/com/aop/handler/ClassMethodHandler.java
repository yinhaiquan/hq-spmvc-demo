package hq.com.aop.handler;

import hq.com.aop.annotation.Parameter;
import hq.com.aop.ctx.SpringApplicationContext;
import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.utils.ClassObject;
import hq.com.aop.utils.KeyObject;
import hq.com.aop.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @title : 方法参数反射处理工具类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/9/5 17:12 星期二
 */
public final class ClassMethodHandler {
    public static Logger log = LoggerFactory.getLogger("proxyHandler");

    /**
     * <p>
     * get Method
     * </p>
     *
     * @param cl
     * @param methodName
     * @param cls
     * @return
     * @throws NoSuchMethodException
     */
    public final static Method getMethod(Class cl, String methodName, Class[] cls) throws NoSuchMethodException {
        return cl.getDeclaredMethod(methodName, cls);
    }

    /**
     * 支持数据入参模型二级参数
     * <p>
     * 若包含字符串和自定义对象，页面请求参数必须跟方法参数顺序一致。
     * 建议入参统一一个入参，即将所有参数封装至入参对象
     * eg:
     * {
     * name:"",
     * info:{
     * name:"",
     * age:""
     * }
     * }
     * </p>
     *
     * @param params
     * @return
     */
    public final static ClassObject formatParams(Map<String, String[]> params) throws IllegalArgumentsException {
        ClassObject co = new ClassObject();
        int i = 0;
        if (null != params) {
            List<KeyObject> set = filterParamsKey(params);
            if (StringUtils.isNotEmpty(set)) {
                Class[] cls = new Class[set.size()];
                Object[] objs = new Object[set.size()];
                for (KeyObject ko : set) {
                    if (ko.isString()) {
                        cls[i] = ko.getTpye();
                        objs[i] = params.get(ko.getKey())[0];
                    } else {
                        cls[i] = ko.getTpye();
                        objs[i] = deSerialization(ko.getKey(), ko.getTpye(), params);
                    }
                    i++;
                }
                co.setCls(cls);
                co.setObjs(objs);
            }
        }
        return co;
    }

    /**
     * 过滤一级参数
     * <p>
     * 参照上面数据模型eg:
     * {
     * name
     * info
     * }
     * </p>
     *
     * @param params
     * @return
     */
    public final static List<KeyObject> filterParamsKey(Map<String, String[]> params) throws IllegalArgumentsException {
        List<KeyObject> set = new ArrayList<KeyObject>();
        boolean tag = true;
        log.info("过滤前参数：[{}]", params);
        for (String key : params.keySet()) {
            KeyObject ko = new KeyObject();
            if (key.contains(StringUtils.PREFIX) && tag) {
                String ky = key.substring(0, key.indexOf(StringUtils.PREFIX));
                ko.setKey(ky);
                ko.setString(false);
                if (StringUtils.isEmpty(AnnotationScannerConfigurer.MAP) || !AnnotationScannerConfigurer.MAP.containsKey(ky)) {
                    throw new IllegalArgumentsException(SpringApplicationContext.getMessage("exception.arguments"), null, null);
                }
                ko.setTpye(AnnotationScannerConfigurer.MAP.get(ky));
                set.add(ko);
                tag = false;
            } else if (!key.contains(StringUtils.PREFIX)) {
                ko.setKey(key);
                ko.setString(true);
                ko.setTpye(String.class);
                set.add(ko);
            }
        }
        log.info("过滤后参数：[{}]", set);
        return set;
    }

    /**
     * 反序列化
     * <p>
     * 通过反射将请求参数封装至入参实体对象
     * </p>
     *
     * @param alias
     * @param cls
     * @param params
     * @return
     * @throws IllegalArgumentsException
     */
    public final static Object deSerialization(String alias, Class cls, Map<String, String[]> params) throws IllegalArgumentsException {
        Object obj = null;
        try {
            obj = SpringApplicationContext.getBean(alias);
            Field[] fs = cls.getDeclaredFields();
            for (Field f : fs) {
                Parameter p = f.getAnnotation(Parameter.class);
                if (StringUtils.isNotEmpty(p)) {
                    PropertyDescriptor pd = new PropertyDescriptor(f.getName(), cls);
                    Method method = pd.getWriteMethod();
                    StringBuffer sb = new StringBuffer(alias);
                    sb.append(StringUtils.PREFIX).append(p.value()).append(StringUtils.SUFFIX);
                    String[] s = params.get(sb.toString());
                    if (StringUtils.isNotEmpty(s) && StringUtils.isNotEmpty(s[0])) {
                        switch (p.type()) {
                            case STRING:
                                method.invoke(obj, s[0]);
                                break;
                            case LONG:
                                method.invoke(obj, Long.parseLong(s[0]));
                                break;
                            case INT:
                                method.invoke(obj, Integer.parseInt(s[0]));
                                break;
                            case DOUBLE:
                                method.invoke(obj, Double.parseDouble(s[0]));
                                break;
                            case FLOAT:
                                method.invoke(obj, Float.parseFloat(s[0]));
                                break;
                        }
                    }
//                    if (p.type().equals(Integer.class)) {
//                        int i = Integer.parseInt(params.get(sb.toString())[0]);
//                        method.invoke(obj, i);
//                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentsException(SpringApplicationContext.getMessage("exception.method.authorizated"), e.getMessage(), e);
        } catch (IntrospectionException e) {
            throw new IllegalArgumentsException(SpringApplicationContext.getMessage("exception.notfind.method"), e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentsException(SpringApplicationContext.getMessage("exception.invocation.target"), e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalArgumentsException(SpringApplicationContext.getMessage("exception.others"), e.getMessage(), e);
        }
        return obj;
    }
}
