package hq.com.quartz;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Locale;

/**
 * 测试quartz获取bean例子，正式环境下可以将QuartzDemo.class写在web子项目，引入datacenter中ctx的工具类，这里只做演示
 */
@Deprecated
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    public static Object getBean(String name) throws ClassNotFoundException {
        try {
            return applicationContext.getBean(name);
        } catch (BeansException e) {
            try {
                Class cls = Class.forName(name);
                return applicationContext.getBean(cls);
            } catch (ClassNotFoundException c) {
                throw new ClassNotFoundException(c.getMessage(), c);
            }
        }
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
