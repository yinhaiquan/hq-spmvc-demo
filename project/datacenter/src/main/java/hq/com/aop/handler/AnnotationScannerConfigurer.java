package hq.com.aop.handler;

import hq.com.aop.annotation.Bean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @title : spring bean loading scan
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/23 13:48 星期二
 */
@Component
@Lazy
public class AnnotationScannerConfigurer implements BeanDefinitionRegistryPostProcessor {
    public static Map<String, Class> MAP = new HashMap<String, Class>();

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

    }

    /**
     * Scan the spring bean to filter custom annotations and add to the cache
     *
     * @param configurableListableBeanFactory
     * @throws BeansException
     */
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        //过滤添加Bean注解入参类
        Map<String, Object> map = configurableListableBeanFactory.getBeansWithAnnotation(Bean.class);
        for (String key : map.keySet()) {
            MAP.put(key, map.get(key).getClass());
        }
    }
}
