package hq.com.aop.ctx;

import hq.com.aop.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import sun.util.locale.BaseLocale;

import java.util.Locale;

/**
 * spring application context
 * <p>
 * <p>
 * You can get beans by SpringApplicationContext. and this Bean's name can be the package name
 * or the name of the class. but must to be configurated in your spring-mybatis.xml.
 * </p>
 *
 * @author yinhaiquan
 * @date 2017/05/22 11:17:26.
 */
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    public static Locale LOCALE = Locale.SIMPLIFIED_CHINESE;

    public static void setLOCALE(String language) {
        switch (language) {
            case "zh_CN":
                LOCALE = Locale.SIMPLIFIED_CHINESE;
                break;
            case "zh_TW":
                LOCALE = Locale.TRADITIONAL_CHINESE;
                break;
            case "en_US":
                LOCALE = Locale.US;
                break;
            case "en_GB":
                LOCALE = Locale.UK;
                break;
            case "en_CA":
                LOCALE = Locale.CANADA;
                break;
            case "ja_JP":
                LOCALE = Locale.JAPAN;
                break;
            case "fr_FR":
                LOCALE = Locale.FRANCE;
                break;
            case "de_DE":
                LOCALE = Locale.GERMANY;
                break;
            case "it_IT":
                LOCALE = Locale.ITALY;
                break;
            case "ko_KR":
                LOCALE = Locale.KOREA;
                break;
            case "fr_CA":
                LOCALE = Locale.CANADA_FRENCH;
                break;
            default:
                LOCALE = Locale.SIMPLIFIED_CHINESE;
                break;
        }
    }

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

    public static String getMessage(String key) {
        return applicationContext.getMessage(key, null, LOCALE);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
